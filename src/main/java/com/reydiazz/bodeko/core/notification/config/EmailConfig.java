package com.reydiazz.bodeko.core.notification.config;

import com.reydiazz.bodeko.core.notification.gateway.EmailSender;
import com.reydiazz.bodeko.core.notification.gateway.LoggingEmailSender;
import com.reydiazz.bodeko.core.notification.gateway.resend.ResendEmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

    private static final String RESEND_PROVIDER = "resend";
    private static final String DEFAULT_RESEND_URL = "https://api.resend.com";
    private static final String DEFAULT_FROM = "Bodeko <onboarding@resend.dev>";

    @Bean
    public EmailSender emailSender(
            @Value("${mail.provider:}") String provider,
            @Value("${mail.from:}") String from,
            @Value("${mail.resend.api-url:}") String apiUrl,
            @Value("${mail.resend.api-key:}") String apiKey
    ) {
        if (RESEND_PROVIDER.equalsIgnoreCase(provider.trim())) {
            return new ResendEmailSender(
                    orDefault(apiUrl, DEFAULT_RESEND_URL),
                    apiKey,
                    orDefault(from, DEFAULT_FROM)
            );
        }
        return new LoggingEmailSender();
    }

    private String orDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

}
