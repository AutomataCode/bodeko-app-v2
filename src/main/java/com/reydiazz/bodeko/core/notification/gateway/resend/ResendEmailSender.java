package com.reydiazz.bodeko.core.notification.gateway.resend;

import com.reydiazz.bodeko.core.notification.gateway.EmailMessage;
import com.reydiazz.bodeko.core.notification.gateway.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.util.List;

@Slf4j
public class ResendEmailSender implements EmailSender {

    private static final String EMAILS_PATH = "/emails";

    private final RestClient restClient;
    private final String from;
    private final boolean configured;

    public ResendEmailSender(String apiUrl, String apiKey, String from) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(10));
        requestFactory.setReadTimeout(Duration.ofSeconds(30));
        this.from = from;
        this.configured = apiKey != null && !apiKey.isBlank();
        this.restClient = RestClient.builder()
                .baseUrl(apiUrl)
                .requestFactory(requestFactory)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    @Override
    public void send(EmailMessage message) {
        if (!configured) {
            log.error("Resend API key is not configured (RESEND_API_KEY), email to {} was not sent", message.to());
            return;
        }
        try {
            restClient.post()
                    .uri(EMAILS_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResendEmailRequest(from, List.of(message.to()), message.subject(), message.html()))
                    .retrieve()
                    .toBodilessEntity();
            log.info("Email \"{}\" sent to {}", message.subject(), message.to());
        } catch (RestClientException exception) {
            log.error("Resend rejected or failed the email to {}", message.to(), exception);
        }
    }

}
