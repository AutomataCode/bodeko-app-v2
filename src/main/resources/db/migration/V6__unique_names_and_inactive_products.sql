-- La migración se detiene si hay duplicados: revisar antes el informe de prevalidación.
CREATE UNIQUE INDEX uq_users_email_normalized ON users (lower(btrim(email)));
CREATE UNIQUE INDEX uq_stores_subdomain_normalized ON stores (lower(btrim(subdomain)));
CREATE UNIQUE INDEX uq_stores_owner_name_normalized ON stores
    (owner_id, lower(regexp_replace(btrim(name), '\s+', ' ', 'g')));
CREATE UNIQUE INDEX uq_products_store_name_normalized ON products
    (store_id, lower(regexp_replace(btrim(name), '\s+', ' ', 'g')));
ALTER TABLE products DROP CONSTRAINT ck_products_status;
ALTER TABLE products ADD CONSTRAINT ck_products_status CHECK (status IN ('DRAFT','ACTIVE','INACTIVE','ARCHIVED'));
CREATE UNIQUE INDEX uq_categories_name_normalized ON categories
    (store_id, lower(regexp_replace(btrim(name), '\s+', ' ', 'g')));
CREATE UNIQUE INDEX uq_payment_approved_order ON payments(order_id) WHERE status='APPROVED';
