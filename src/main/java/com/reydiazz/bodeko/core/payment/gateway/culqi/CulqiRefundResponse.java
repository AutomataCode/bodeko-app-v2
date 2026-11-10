package com.reydiazz.bodeko.core.payment.gateway.culqi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CulqiRefundResponse(

        @JsonProperty("object")
        String object,

        @JsonProperty("id")
        String id,

        @JsonProperty("charge_id")
        String chargeId,

        @JsonProperty("status")
        String status,

        @JsonProperty("type")
        String type,

        @JsonProperty("user_message")
        String userMessage,

        @JsonProperty("merchant_message")
        String merchantMessage

) {
}
