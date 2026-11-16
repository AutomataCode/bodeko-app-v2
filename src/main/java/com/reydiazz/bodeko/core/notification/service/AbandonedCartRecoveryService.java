package com.reydiazz.bodeko.core.notification.service;

import com.reydiazz.bodeko.core.cart.items.repository.ShoppingCartItemRepository;
import com.reydiazz.bodeko.core.notification.gateway.EmailMessage;
import com.reydiazz.bodeko.core.notification.gateway.EmailSender;
import com.reydiazz.bodeko.core.shopping.cart.model.entity.ShoppingCart;
import com.reydiazz.bodeko.core.shopping.cart.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import java.time.OffsetDateTime;

/** Envía un único recordatorio a clientes que aceptaron promociones. */
@Service
@RequiredArgsConstructor
@Slf4j
public class AbandonedCartRecoveryService {
    private final ShoppingCartRepository carts;
    private final ShoppingCartItemRepository items;
    private final EmailSender emailSender;
    @Value("${app.frontend-base-url:http://localhost:3000}") private String frontendUrl;

    @Scheduled(fixedDelayString = "${cart.recovery.interval-ms:1800000}")
    @Transactional
    public void recoverAbandonedCarts() {
        OffsetDateTime cutoff = OffsetDateTime.now().minusHours(24);
        for (var id : carts.findRecoverableCartIds(cutoff)) {
            try {
                ShoppingCart cart = carts.findByIdForRecovery(id).orElse(null);
                if (cart == null || cart.getUpdatedAt().isAfter(cutoff) || !cart.getUser().isMarketingConsent()) continue;
                var cartItems = items.findAllWithProductByCartId(id);
                if (cartItems.isEmpty() || !cart.getUser().isMarketingConsent()) continue;
                String link = frontendUrl.replaceAll("/$", "") + "/tienda/" + cart.getStore().getSubdomain() + "/carrito";
                String rows = cartItems.stream().map(item -> "<li>" + HtmlUtils.htmlEscape(item.getProduct().getName()) + " — " + item.getQuantity() + " unidad(es)</li>").collect(java.util.stream.Collectors.joining());
                String html = "<p>Hola " + HtmlUtils.htmlEscape(cart.getUser().getFullName()) + ",</p><p>Guardamos estos productos en tu carrito de " + HtmlUtils.htmlEscape(cart.getStore().getName()) + ":</p><ul>" + rows + "</ul><p><a href=\"" + HtmlUtils.htmlEscape(link) + "\">Volver a mi carrito</a></p><small>Recibes este mensaje porque aceptaste recibir novedades y promociones.</small>";
                emailSender.send(new EmailMessage(cart.getUser().getEmail(), "Tu carrito sigue listo en " + cart.getStore().getName(), html));
                cart.markRecoveryEmailSent();
                log.info("Se procesó recordatorio de carrito para la tienda {}", cart.getStore().getId());
            } catch (RuntimeException exception) {
                log.error("No se pudo procesar el recordatorio de un carrito", exception);
            }
        }
    }
}
