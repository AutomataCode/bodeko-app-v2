package com.reydiazz.bodeko.security.util;

import com.reydiazz.bodeko.shared.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@UtilityClass
public class HttpResponse {

    private final String CHARACTER_ENCODING = "UTF-8";

    public void sendError(
            ErrorResponse error,
            HttpServletResponse response,
            ObjectMapper objectMapper
    ) throws IOException {
        response.setStatus(error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }

}