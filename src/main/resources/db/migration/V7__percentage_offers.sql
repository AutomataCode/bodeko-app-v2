-- El importe anterior se conserva por compatibilidad e historial.
ALTER TABLE product_offers ADD COLUMN discount_percentage NUMERIC(5,2);
ALTER TABLE product_offers ADD CONSTRAINT ck_offer_percentage
    CHECK (discount_percentage IS NULL OR discount_percentage BETWEEN 0 AND 99.99);
-- Solo convertir cuando el porcentaje de dos decimales reproduce exactamente el importe previo.
WITH candidates AS (
 SELECT o.product_id,o.starts_at,o.ends_at,p.base_price,o.offer_price,
        round(100*(1-o.offer_price/NULLIF(p.base_price,0)),2) AS percentage
 FROM product_offers o JOIN products p ON p.id=o.product_id WHERE p.base_price>0 AND o.offer_price>0
)
UPDATE product_offers o SET discount_percentage=c.percentage FROM candidates c
WHERE o.product_id=c.product_id AND o.starts_at=c.starts_at AND o.ends_at=c.ends_at
 AND c.percentage BETWEEN 0 AND 99.99
 AND round(c.base_price*(1-c.percentage/100),2)=c.offer_price;
