package com.reydiazz.bodeko.core.payment.gateway.culqi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record CulqiChargeRequest(

        @JsonProperty("amount")
        int amount,

        @JsonProperty("currency_code")
        String currencyCode,

        @JsonProperty("email")
        String email,

        @JsonProperty("source_id")
        String sourceId,

        @JsonProperty("description")
        String description,

        @JsonProperty("metadata")
        Map<String, String> metadata

) {
}
