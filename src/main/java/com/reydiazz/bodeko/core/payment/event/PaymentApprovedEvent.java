package com.reydiazz.bodeko.core.payment.event;

import java.util.UUID;

public record PaymentApprovedEvent(UUID orderId) {
}
