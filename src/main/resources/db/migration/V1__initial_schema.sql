CREATE TABLE users
(
    id              UUID PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    full_name       VARCHAR(150) NOT NULL,
    document_type   VARCHAR(20)           DEFAULT 'DNI',
    document_number VARCHAR(20),
    business_name   VARCHAR(200),
    billing_address VARCHAR(300),
    role            VARCHAR(20)  NOT NULL DEFAULT 'CUSTOMER',
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT ck_users_role
        CHECK (role IN ('ADMIN', 'CUSTOMER'))
);

CREATE TABLE stores
(
    id         UUID PRIMARY KEY,
    owner_id   UUID          NOT NULL,
    name       VARCHAR(150)  NOT NULL,
    tax_id     CHAR(11)      NOT NULL UNIQUE,
    legal_name VARCHAR(200)  NOT NULL,
    subdomain  VARCHAR(63)   NOT NULL UNIQUE,
    currency   CHAR(3)       NOT NULL DEFAULT 'PEN',
    tax_rate   NUMERIC(4, 2) NOT NULL DEFAULT 0.18,
    created_at TIMESTAMPTZ   NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_stores_owner
        FOREIGN KEY (owner_id)
            REFERENCES users (id)
            ON DELETE RESTRICT
);

CREATE INDEX idx_stores_owner_id
    ON stores (owner_id);

CREATE TABLE categories
(
    id       UUID PRIMARY KEY,
    store_id UUID         NOT NULL,
    name     VARCHAR(100) NOT NULL,

    CONSTRAINT fk_categories_store
        FOREIGN KEY (store_id)
            REFERENCES stores (id)
            ON DELETE CASCADE,

    CONSTRAINT uq_categories_store_name
        UNIQUE (store_id, name),

    CONSTRAINT uq_categories_id_store
        UNIQUE (id, store_id)
);

CREATE TABLE products
(
    id            UUID PRIMARY KEY,
    store_id      UUID           NOT NULL,
    category_id   UUID,
    name          VARCHAR(200)   NOT NULL,
    description   TEXT,
    base_price    NUMERIC(12, 2) NOT NULL,
    image_url     VARCHAR(500),
    is_tax_exempt BOOLEAN        NOT NULL DEFAULT FALSE,
    status        VARCHAR(20)    NOT NULL DEFAULT 'DRAFT',
    created_at    TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT ck_products_base_price
        CHECK (base_price >= 0),

    CONSTRAINT ck_products_status
        CHECK (status IN ('ACTIVE', 'DRAFT', 'ARCHIVED')),

    CONSTRAINT fk_products_store
        FOREIGN KEY (store_id)
            REFERENCES stores (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_products_category_store
        FOREIGN KEY (category_id, store_id)
            REFERENCES categories (id, store_id)
            ON DELETE RESTRICT,

    CONSTRAINT uq_products_id_store
        UNIQUE (id, store_id)
);

CREATE INDEX idx_products_store_id
    ON products (store_id);

CREATE INDEX idx_products_category_id
    ON products (category_id);

CREATE INDEX idx_products_store_status
    ON products (store_id, status);

CREATE TABLE inventory
(
    id                  UUID PRIMARY KEY,
    product_id          UUID        NOT NULL UNIQUE,
    quantity            INTEGER     NOT NULL DEFAULT 0,
    low_stock_threshold INTEGER     NOT NULL DEFAULT 5,
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT ck_inventory_quantity
        CHECK (quantity >= 0),

    CONSTRAINT ck_inventory_low_stock_threshold
        CHECK (low_stock_threshold >= 0),

    CONSTRAINT fk_inventory_product
        FOREIGN KEY (product_id)
            REFERENCES products (id)
            ON DELETE CASCADE
);

CREATE TABLE product_offers
(
    store_id    UUID           NOT NULL,
    product_id  UUID           NOT NULL,
    offer_price NUMERIC(12, 2) NOT NULL,
    starts_at   TIMESTAMPTZ    NOT NULL,
    ends_at     TIMESTAMPTZ    NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_product_offers
        PRIMARY KEY (product_id, starts_at, ends_at),

    CONSTRAINT ck_product_offers_price
        CHECK (offer_price >= 0),

    CONSTRAINT ck_product_offers_dates
        CHECK (ends_at > starts_at),

    CONSTRAINT fk_product_offers_product
        FOREIGN KEY (product_id, store_id)
            REFERENCES products (id, store_id)
            ON DELETE CASCADE
);

CREATE INDEX idx_product_offers_store_id
    ON product_offers (store_id);

CREATE TABLE shopping_carts
(
    id         UUID PRIMARY KEY,
    user_id    UUID        NOT NULL,
    store_id   UUID        NOT NULL,
    status     VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT ck_shopping_carts_status
        CHECK (status IN ('OPEN', 'CONVERTED', 'ABANDONED')),

    CONSTRAINT fk_shopping_carts_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_shopping_carts_store
        FOREIGN KEY (store_id)
            REFERENCES stores (id)
            ON DELETE CASCADE,

    CONSTRAINT uq_shopping_carts_id_store
        UNIQUE (id, store_id)
);

CREATE INDEX idx_shopping_carts_user_store
    ON shopping_carts (user_id, store_id);

CREATE INDEX idx_shopping_carts_store_id
    ON shopping_carts (store_id);

CREATE TABLE shopping_cart_items
(
    store_id   UUID    NOT NULL,
    cart_id    UUID    NOT NULL,
    product_id UUID    NOT NULL,
    quantity   INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT pk_shopping_cart_items
        PRIMARY KEY (cart_id, product_id),

    CONSTRAINT ck_shopping_cart_items_quantity
        CHECK (quantity > 0),

    CONSTRAINT fk_shopping_cart_items_cart
        FOREIGN KEY (cart_id, store_id)
            REFERENCES shopping_carts (id, store_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_shopping_cart_items_product
        FOREIGN KEY (product_id, store_id)
            REFERENCES products (id, store_id)
            ON DELETE CASCADE
);

CREATE INDEX idx_shopping_cart_items_product_store
    ON shopping_cart_items (product_id, store_id);

CREATE TABLE orders
(
    id                       UUID PRIMARY KEY,
    store_id                 UUID           NOT NULL,
    customer_id              UUID           NOT NULL,
    order_number             VARCHAR(30)    NOT NULL UNIQUE,

    invoice_type             VARCHAR(20)    NOT NULL DEFAULT 'RECEIPT',
    customer_document_type   VARCHAR(20)    NOT NULL,
    customer_document_number VARCHAR(20)    NOT NULL,
    customer_business_name   VARCHAR(200),
    customer_billing_address VARCHAR(300),

    subtotal                 NUMERIC(12, 2) NOT NULL,
    tax_amount               NUMERIC(12, 2) NOT NULL,
    total                    NUMERIC(12, 2) NOT NULL,
    applied_tax_rate         NUMERIC(4, 2)  NOT NULL,

    status                   VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    created_at               TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT ck_orders_subtotal
        CHECK (subtotal >= 0),

    CONSTRAINT ck_orders_tax_amount
        CHECK (tax_amount >= 0),

    CONSTRAINT ck_orders_total
        CHECK (total >= 0),

    CONSTRAINT ck_orders_invoice_type
        CHECK (invoice_type IN ('RECEIPT', 'INVOICE')),

    CONSTRAINT ck_orders_status
        CHECK (
            status IN (
                       'PENDING',
                       'PAID',
                       'SHIPPED',
                       'DELIVERED',
                       'CANCELLED'
                )
            ),

    CONSTRAINT fk_orders_store
        FOREIGN KEY (store_id)
            REFERENCES stores (id)
            ON DELETE RESTRICT,

    CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id)
            REFERENCES users (id)
            ON DELETE RESTRICT,

    CONSTRAINT uq_orders_id_store
        UNIQUE (id, store_id)
);

CREATE INDEX idx_orders_store_status
    ON orders (store_id, status);

CREATE INDEX idx_orders_customer_id
    ON orders (customer_id);

CREATE INDEX idx_orders_created_at
    ON orders (created_at);

CREATE TABLE order_items
(
    store_id           UUID           NOT NULL,
    order_id           UUID           NOT NULL,
    product_id         UUID           NOT NULL,
    product_name       VARCHAR(200)   NOT NULL,
    quantity           INTEGER        NOT NULL,

    list_unit_price    NUMERIC(12, 2) NOT NULL,
    charged_unit_price NUMERIC(12, 2) NOT NULL,
    item_subtotal      NUMERIC(12, 2) NOT NULL,

    CONSTRAINT pk_order_items
        PRIMARY KEY (order_id, product_id),

    CONSTRAINT ck_order_items_quantity
        CHECK (quantity > 0),

    CONSTRAINT ck_order_items_list_unit_price
        CHECK (list_unit_price >= 0),

    CONSTRAINT ck_order_items_charged_unit_price
        CHECK (charged_unit_price >= 0),

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id, store_id)
            REFERENCES orders (id, store_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_order_items_product
        FOREIGN KEY (product_id, store_id)
            REFERENCES products (id, store_id)
            ON DELETE RESTRICT
);

CREATE INDEX idx_order_items_product_store
    ON order_items (product_id, store_id);

CREATE TABLE payments
(
    id             UUID PRIMARY KEY,
    order_id       UUID           NOT NULL,
    provider       VARCHAR(30)    NOT NULL,
    transaction_id VARCHAR(100)   NOT NULL UNIQUE,
    amount         NUMERIC(12, 2) NOT NULL,
    status         VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT ck_payments_amount
        CHECK (amount > 0),

    CONSTRAINT ck_payments_status
        CHECK (
            status IN (
                       'PENDING',
                       'APPROVED',
                       'FAILED',
                       'REFUNDED'
                )
            ),

    CONSTRAINT fk_payments_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id)
            ON DELETE RESTRICT
);

CREATE INDEX idx_payments_order_id
    ON payments (order_id);
