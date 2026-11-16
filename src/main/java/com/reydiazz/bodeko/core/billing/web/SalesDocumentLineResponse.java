package com.reydiazz.bodeko.core.billing.web;

import java.math.BigDecimal;
import java.util.Map;

/** Línea congelada de producto incluida en el comprobante. */
public record SalesDocumentLineResponse(String productName, String sku, Map<String, String> options,
                                        int quantity, BigDecimal unitPrice, BigDecimal subtotal) {}
