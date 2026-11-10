package com.reydiazz.bodeko.core.payment.gateway.culqi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CulqiChargeResponse(

        @JsonProperty("object")
        String object,

        @JsonProperty("id")
        String id,

        @JsonProperty("charge_id")
        String chargeId,

        @JsonProperty("type")
        String type,

        @JsonProperty("code")
        String code,

        @JsonProperty("decline_code")
        String declineCode,

        @JsonProperty("user_message")
        String userMessage,

        @JsonProperty("merchant_message")
        String merchantMessage,

        @JsonProperty("outcome")
        Outcome outcome

) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Outcome(

            @JsonProperty("type")
            String type,

            @JsonProperty("user_message")
            String userMessage

    ) {
    }

}
