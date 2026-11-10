package com.reydiazz.bodeko.core.notification.component;

import com.reydiazz.bodeko.core.notification.service.NotificationService;
import com.reydiazz.bodeko.core.payment.event.PaymentApprovedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentApprovedListener {

    private final NotificationService notificationService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentApproved(PaymentApprovedEvent event) {
        notificationService.sendOrderPaidConfirmation(event.orderId());
    }

}
