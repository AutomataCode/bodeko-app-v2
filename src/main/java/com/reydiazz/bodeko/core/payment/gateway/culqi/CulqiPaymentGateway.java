package com.reydiazz.bodeko.core.payment.gateway.culqi;

import com.reydiazz.bodeko.core.payment.exception.PaymentErrorCode;
import com.reydiazz.bodeko.core.payment.exception.PaymentGatewayException;
import com.reydiazz.bodeko.core.payment.gateway.GatewayChargeRequest;
import com.reydiazz.bodeko.core.payment.gateway.GatewayChargeResult;
import com.reydiazz.bodeko.core.payment.gateway.GatewayRefundRequest;
import com.reydiazz.bodeko.core.payment.gateway.GatewayRefundResult;
import com.reydiazz.bodeko.core.payment.gateway.PaymentGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
public class CulqiPaymentGateway implements PaymentGateway {

    private static final String PROVIDER = "CULQI";
    private static final String DEFAULT_API_URL = "https://api.culqi.com/v2";
    private static final String CHARGES_PATH = "/charges";
    private static final String CHARGE_OBJECT = "charge";
    private static final String REFUNDS_PATH = "/refunds";
    private static final String REFUND_OBJECT = "refund";
    private static final String REFUND_REASON = "solicitud_comprador";
    private static final String REFUND_REJECTED_STATUS = "rechazado";
    private static final String CARD_ERROR = "card_error";
    private static final int PAYMENT_REQUIRED = 402;

    private final RestClient restClient;
    private final boolean configured;
    private final com.reydiazz.bodeko.shared.idempotency.IdempotencyService idempotency;

    public CulqiPaymentGateway(
            @Value("${culqi.api-url}") String apiUrl,
            @Value("${culqi.secret-key}") String secretKey,
            com.reydiazz.bodeko.shared.idempotency.IdempotencyService idempotency
    ) {
        this.idempotency=idempotency;
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(10));
        requestFactory.setReadTimeout(Duration.ofSeconds(30));
        this.configured = secretKey != null && !secretKey.isBlank();
        this.restClient = RestClient.builder()
                .baseUrl(apiUrl == null || apiUrl.isBlank() ? DEFAULT_API_URL : apiUrl.trim())
                .requestFactory(new BufferingClientHttpRequestFactory(requestFactory))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
                .build();
    }

    @Override
    public String provider() {
        return PROVIDER;
    }

    @Override
    public GatewayChargeResult charge(GatewayChargeRequest request) {
        if (!configured) {
            log.error("Culqi secret key is not configured (CULQI_SECRET_KEY)");
            throw new PaymentGatewayException(PaymentErrorCode.GATEWAY_UNAVAILABLE);
        }
        CulqiChargeRequest body = new CulqiChargeRequest(
                toCents(request.amount()),
                request.currency(),
                request.email(),
                request.sourceId(),
                "Orden " + request.orderNumber(),
                Map.of("order_number", request.orderNumber())
        );
        idempotency.markExternalStarted();
        try {
            return restClient.post()
                    .uri(CHARGES_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .exchange((httpRequest, httpResponse) ->
                            toResult(httpResponse.getStatusCode(), httpResponse.bodyTo(CulqiChargeResponse.class)));
        } catch (RestClientException exception) {
            log.error("No se pudo confirmar la respuesta del cobro de Culqi");
            throw new PaymentGatewayException(PaymentErrorCode.GATEWAY_UNAVAILABLE);
        }
    }

    private GatewayChargeResult toResult(HttpStatusCode status, CulqiChargeResponse response) {
        if (response == null) {
            throw new PaymentGatewayException(PaymentErrorCode.GATEWAY_UNAVAILABLE);
        }
        if (status.is2xxSuccessful() && CHARGE_OBJECT.equals(response.object()) && response.id() != null) {
            String message = response.outcome() != null ? response.outcome().userMessage() : null;
            return new GatewayChargeResult(response.id(), true, message);
        }
        if (status.value() == PAYMENT_REQUIRED && CARD_ERROR.equals(response.type()) && response.chargeId() != null) {
            log.info("Culqi charge {} declined: {} ({})",
                    response.chargeId(), response.merchantMessage(), response.declineCode());
            return new GatewayChargeResult(response.chargeId(), false, response.userMessage());
        }
        log.warn("Culqi rejected the request with status {}: {} - {}",
                status.value(), response.type(), response.merchantMessage());
        if (status.is4xxClientError()) {
            throw new PaymentGatewayException(response.userMessage(), PaymentErrorCode.GATEWAY_INVALID_REQUEST);
        }
        throw new PaymentGatewayException(PaymentErrorCode.GATEWAY_UNAVAILABLE);
    }

    @Override
    public GatewayRefundResult refund(GatewayRefundRequest request) {
        if (!configured) {
            log.error("Culqi secret key is not configured (CULQI_SECRET_KEY)");
            throw new PaymentGatewayException(PaymentErrorCode.GATEWAY_UNAVAILABLE);
        }
        CulqiRefundRequest body = new CulqiRefundRequest(
                toCents(request.amount()),
                request.transactionId(),
                REFUND_REASON
        );
        idempotency.markExternalStarted();
        try {
            return restClient.post()
                    .uri(REFUNDS_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .exchange((httpRequest, httpResponse) ->
                            toRefundResult(httpResponse.getStatusCode(), httpResponse.bodyTo(CulqiRefundResponse.class)));
        } catch (RestClientException exception) {
            log.error("No se pudo confirmar la respuesta del reembolso de Culqi");
            throw new PaymentGatewayException(PaymentErrorCode.GATEWAY_UNAVAILABLE);
        }
    }

    private GatewayRefundResult toRefundResult(HttpStatusCode status, CulqiRefundResponse response) {
        if (response == null) {
            throw new PaymentGatewayException(PaymentErrorCode.GATEWAY_UNAVAILABLE);
        }
        if (status.is2xxSuccessful() && REFUND_OBJECT.equals(response.object()) && response.id() != null) {
            if (REFUND_REJECTED_STATUS.equalsIgnoreCase(response.status())) {
                log.warn("Culqi refund {} was rejected", response.id());
                return new GatewayRefundResult(response.id(), false, null);
            }
            return new GatewayRefundResult(response.id(), true, null);
        }
        log.warn("Culqi rejected the refund with status {}: {} - {}",
                status.value(), response.type(), response.merchantMessage());
        if (status.is4xxClientError()) {
            return new GatewayRefundResult(null, false, response.userMessage());
        }
        throw new PaymentGatewayException(PaymentErrorCode.GATEWAY_UNAVAILABLE);
    }

    private int toCents(BigDecimal amount) {
        try {
            int cents=amount.setScale(2, RoundingMode.UNNECESSARY).movePointRight(2).intValueExact();
            if (cents<=0) throw new ArithmeticException();
            return cents;
        } catch (ArithmeticException error) {
            throw new com.reydiazz.bodeko.core.payment.exception.PaymentInvalidAmountException();
        }
    }

}
