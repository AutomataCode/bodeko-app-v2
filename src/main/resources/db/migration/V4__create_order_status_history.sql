CREATE TABLE order_status_history
(
    id          UUID PRIMARY KEY,
    order_id    UUID        NOT NULL,
    from_status VARCHAR(20) NOT NULL,
    to_status   VARCHAR(20) NOT NULL,
    changed_by  UUID        NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT ck_order_status_history_from_status
        CHECK (
            from_status IN (
                            'PENDING',
                            'PAID',
                            'SHIPPED',
                            'DELIVERED',
                            'CANCELLED'
                )
            ),

    CONSTRAINT ck_order_status_history_to_status
        CHECK (
            to_status IN (
                          'PENDING',
                          'PAID',
                          'SHIPPED',
                          'DELIVERED',
                          'CANCELLED'
                )
            ),

    CONSTRAINT fk_order_status_history_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_order_status_history_user
        FOREIGN KEY (changed_by)
            REFERENCES users (id)
            ON DELETE RESTRICT
);

CREATE INDEX idx_order_status_history_order_created
    ON order_status_history (order_id, created_at);
