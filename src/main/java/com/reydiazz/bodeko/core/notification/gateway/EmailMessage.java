package com.reydiazz.bodeko.core.notification.gateway;

public record EmailMessage(
        String to,
        String subject,
        String html
) {
}
