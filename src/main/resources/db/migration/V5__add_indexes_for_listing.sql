CREATE INDEX IF NOT EXISTS idx_products_status
    ON products (status);

CREATE INDEX IF NOT EXISTS idx_products_price
    ON products (price);

CREATE INDEX IF NOT EXISTS idx_products_created_at
    ON products (created_at);

CREATE INDEX IF NOT EXISTS idx_orders_user_status_created_at
    ON orders (user_id, status, created_at);
