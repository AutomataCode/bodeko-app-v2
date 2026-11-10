package com.reydiazz.bodeko.core.payment.repository;

import com.reydiazz.bodeko.core.payment.model.entity.Payment;
import com.reydiazz.bodeko.core.payment.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findFirstByOrderIdAndStatus(UUID orderId, PaymentStatus status);

}
