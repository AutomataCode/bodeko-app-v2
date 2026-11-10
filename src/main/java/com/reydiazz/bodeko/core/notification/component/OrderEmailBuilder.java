package com.reydiazz.bodeko.core.notification.component;

import com.reydiazz.bodeko.core.notification.gateway.EmailMessage;
import com.reydiazz.bodeko.core.order.core.model.entity.Order;
import com.reydiazz.bodeko.core.order.items.model.entity.OrderItem;
import com.reydiazz.bodeko.core.store.model.enums.TypeCurrency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class OrderEmailBuilder {

    private static final String TEMPLATE = "email/order-paid";
    private static final Locale LOCALE = Locale.forLanguageTag("es-PE");

    private final TemplateEngine templateEngine;

    public EmailMessage buildOrderPaid(Order order, List<OrderItem> items) {
        String currency = symbol(order.getStore().getCurrency());

        Context context = new Context(LOCALE);
        context.setVariable("storeName", order.getStore().getName());
        context.setVariable("customerName", order.getCustomer().getFullName());
        context.setVariable("orderNumber", order.getOrderNumber());
        context.setVariable("invoiceType", order.getInvoiceType().name());
        context.setVariable("documentNumber", order.getCustomerDocumentNumber());
        context.setVariable("items", items.stream().map(item -> toView(item, currency)).toList());
        context.setVariable("subtotal", money(currency, order.getSubtotal()));
        context.setVariable("taxAmount", money(currency, order.getTaxAmount()));
        context.setVariable("total", money(currency, order.getTotal()));

        String html = templateEngine.process(TEMPLATE, context);
        String subject = "Confirmación de tu compra " + order.getOrderNumber() + " - " + order.getStore().getName();
        return new EmailMessage(order.getCustomer().getEmail(), subject, html);
    }

    private ItemView toView(OrderItem item, String currency) {
        return new ItemView(
                item.getProductName(),
                item.getQuantity(),
                money(currency, item.getChargedUnitPrice()),
                money(currency, item.getItemSubtotal())
        );
    }

    private String money(String currency, BigDecimal amount) {
        return currency + " " + amount.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }

    private String symbol(TypeCurrency currency) {
        return switch (currency) {
            case PEN -> "S/";
            case USD -> "US$";
            case EUR -> "EUR";
        };
    }

    public record ItemView(String name, int quantity, String unitPrice, String subtotal) {
    }

}
