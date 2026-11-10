package com.reydiazz.bodeko.core.notification.gateway.resend;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ResendEmailRequest(

        @JsonProperty("from")
        String from,

        @JsonProperty("to")
        List<String> to,

        @JsonProperty("subject")
        String subject,

        @JsonProperty("html")
        String html

) {
}
