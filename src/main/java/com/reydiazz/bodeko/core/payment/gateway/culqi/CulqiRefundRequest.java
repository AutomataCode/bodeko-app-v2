package com.reydiazz.bodeko.core.payment.gateway.culqi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CulqiRefundRequest(

        @JsonProperty("amount")
        int amount,

        @JsonProperty("charge_id")
        String chargeId,

        @JsonProperty("reason")
        String reason

) {
}
