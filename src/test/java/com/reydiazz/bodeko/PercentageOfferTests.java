package com.reydiazz.bodeko;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import com.reydiazz.bodeko.core.product.offer.model.entity.ProductOffer;
import com.reydiazz.bodeko.core.product.core.model.entity.Product;

/** Reglas monetarias y compatibilidad de promociones históricas. */
class PercentageOfferTests {
 @Test void percentageRoundsHalfUp() { assertEquals(new BigDecimal("7.50"),ProductOffer.percentagePrice(new BigDecimal("9.99"),new BigDecimal("24.90"))); }
 @Test void percentageUsesCurrentBasePrice() {
  ProductOffer offer=ProductOffer.builder().product(Product.builder().basePrice(new BigDecimal("40.00")).build()).offerPrice(new BigDecimal("15.00")).discountPercentage(new BigDecimal("25.00")).build();
  assertEquals(new BigDecimal("30.00"),offer.getOfferPrice());
 }
 @Test void legacyOfferPreservesAmount() { assertEquals(new BigDecimal("15.00"),ProductOffer.builder().offerPrice(new BigDecimal("15.00")).build().getOfferPrice()); }
 @Test void smallestCurrencyUnitRemainsPayable() { assertEquals(new BigDecimal("0.01"),ProductOffer.percentagePrice(new BigDecimal("0.01"),new BigDecimal("99.99"))); }
}
