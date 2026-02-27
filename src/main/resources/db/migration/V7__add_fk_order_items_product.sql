DO $$
DECLARE
    orphan_count BIGINT;
BEGIN
    SELECT COUNT(*)
      INTO orphan_count
      FROM order_items oi
 LEFT JOIN products p
        ON p.id = oi.product_id
     WHERE p.id IS NULL;

    IF orphan_count > 0 THEN
        RAISE EXCEPTION
            'Cannot add FK order_items.product_id -> products.id. Found % orphan rows in order_items.',
            orphan_count;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_order_items_product_id
    ON order_items (product_id);

ALTER TABLE order_items
    ADD CONSTRAINT fk_order_items_product
        FOREIGN KEY (product_id)
            REFERENCES products (id)
            ON DELETE RESTRICT;
