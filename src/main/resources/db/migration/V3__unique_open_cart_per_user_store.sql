CREATE UNIQUE INDEX uq_shopping_carts_open_user_store
    ON shopping_carts (user_id, store_id)
    WHERE status = 'OPEN';
