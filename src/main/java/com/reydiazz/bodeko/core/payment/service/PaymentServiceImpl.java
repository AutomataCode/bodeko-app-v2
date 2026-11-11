package com.reydiazz.bodeko.core.payment.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.order.core.model.entity.Order;
import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.core.order.core.repository.OrderRepository;
import com.reydiazz.bodeko.core.payment.component.PaymentMapper;
import com.reydiazz.bodeko.core.payment.event.PaymentApprovedEvent;
import com.reydiazz.bodeko.core.payment.exception.PaymentInvalidAmountException;
import com.reydiazz.bodeko.core.payment.exception.PaymentOrderNotFoundException;
import com.reydiazz.bodeko.core.payment.exception.PaymentOrderNotPendingException;
import com.reydiazz.bodeko.core.payment.exception.PaymentRefundRejectedException;
import com.reydiazz.bodeko.core.payment.gateway.GatewayChargeRequest;
import com.reydiazz.bodeko.core.payment.gateway.GatewayChargeResult;
import com.reydiazz.bodeko.core.payment.gateway.GatewayRefundRequest;
import com.reydiazz.bodeko.core.payment.gateway.GatewayRefundResult;
import com.reydiazz.bodeko.core.payment.gateway.PaymentGateway;
import com.reydiazz.bodeko.core.payment.model.entity.Payment;
import com.reydiazz.bodeko.core.payment.model.enums.PaymentStatus;
import com.reydiazz.bodeko.core.payment.repository.PaymentRepository;
import com.reydiazz.bodeko.core.payment.web.request.CreatePaymentRequest;
import com.reydiazz.bodeko.core.payment.web.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentGateway paymentGateway;

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final jakarta.persistence.EntityManager entityManager;

    @Override
    @Transactional
    public PaymentResponse pay(UUID userId, UUID storeId, UUID orderId, CreatePaymentRequest request) {
        Order order = orderRepository.findByIdAndStoreIdAndCustomerIdForUpdate(orderId, storeId, userId)
                .orElseThrow(PaymentOrderNotFoundException::new);
        // La comprobación previa del controlador pudo cargar una versión anterior.
        entityManager.refresh(order);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new PaymentOrderNotPendingException();
        }
        BigDecimal amount = order.getTotal();
        verifyAmount(amount);

        GatewayChargeResult result = paymentGateway.charge(new GatewayChargeRequest(
                amount,
                order.getStore().getCurrency().name(),
                order.getCustomer().getEmail(),
                request.sourceId().trim(),
                order.getOrderNumber()
        ));

        Payment payment = registerPending(order, result.transactionId(), amount);
        if (result.approved()) {
            confirmApproved(payment.getTransactionId());
        } else {
            markAsFailed(payment.getTransactionId());
        }
        return paymentMapper.toResponse(payment, order, result.message());
    }

    @Override
    @Transactional
    public void refundApprovedPayment(UUID orderId) {
        Payment payment = paymentRepository.findFirstByOrderIdAndStatus(orderId, PaymentStatus.APPROVED)
                .orElse(null);
        if (payment == null) {
            log.warn("Order {} has no approved payment to refund", orderId);
            return;
        }
        GatewayRefundResult result = paymentGateway.refund(new GatewayRefundRequest(
                payment.getTransactionId(),
                payment.getAmount(),
                payment.getOrder().getOrderNumber()
        ));
        if (!result.accepted()) {
            throw new PaymentRefundRejectedException(result.message());
        }
        payment.markAsRefunded();
        log.info("Payment {} of order {} refunded (refund {})", payment.getId(), orderId, result.refundId());
    }

    private void verifyAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0 || amount.movePointRight(2).compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0) {
            throw new PaymentInvalidAmountException();
        }
    }

    private Payment registerPending(Order order, String transactionId, BigDecimal amount) {
        Payment payment = Payment.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .order(order)
                .provider(paymentGateway.provider())
                .transactionId(transactionId)
                .amount(amount)
                .build();
        return paymentRepository.save(payment);
    }

    private void confirmApproved(String transactionId) {
        Payment payment = findByTransactionId(transactionId);
        payment.markAsApproved();
        payment.getOrder().markAsPaid();
        eventPublisher.publishEvent(new PaymentApprovedEvent(payment.getOrder().getId()));
    }

    private void markAsFailed(String transactionId) {
        Payment payment = findByTransactionId(transactionId);
        payment.markAsFailed();
    }

    private Payment findByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(IllegalStateException::new);
    }

}
