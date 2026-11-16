package com.reydiazz.bodeko.core.order.tracking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

/** Datos de la empresa, número y fecha estimada del envío. */
public record UpdateTrackingRequest(@NotBlank @Size(max = 100) String carrier,
                                    @NotBlank @Size(max = 120) String trackingNumber,
                                    @Size(max = 500) @Pattern(regexp = "^$|^https?://[^\s]+$", message = "La URL debe comenzar con http:// o https://") String trackingUrl,
                                    OffsetDateTime estimatedDeliveryAt) {}
