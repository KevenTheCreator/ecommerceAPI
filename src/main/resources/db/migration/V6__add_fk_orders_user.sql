DO $$
DECLARE
    orphan_count BIGINT;
BEGIN
    SELECT COUNT(*)
      INTO orphan_count
      FROM orders o
 LEFT JOIN users u
        ON u.id = o.user_id
     WHERE u.id IS NULL;

    IF orphan_count > 0 THEN
        RAISE EXCEPTION
            'Cannot add FK orders.user_id -> users.id. Found % orphan rows in orders.',
            orphan_count;
    END IF;
END $$;

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE RESTRICT;
