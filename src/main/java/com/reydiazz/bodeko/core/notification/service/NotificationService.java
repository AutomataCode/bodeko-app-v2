package com.reydiazz.bodeko.core.notification.service;

import java.util.UUID;

public interface NotificationService {

    void sendOrderPaidConfirmation(UUID orderId);

}
