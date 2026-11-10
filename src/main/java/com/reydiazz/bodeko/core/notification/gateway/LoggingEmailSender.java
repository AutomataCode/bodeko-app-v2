package com.reydiazz.bodeko.core.notification.gateway;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingEmailSender implements EmailSender {

    @Override
    public void send(EmailMessage message) {
        log.info("[MOCK EMAIL] to={} subject=\"{}\" ({} chars of html)",
                message.to(), message.subject(), message.html().length());
    }

}
