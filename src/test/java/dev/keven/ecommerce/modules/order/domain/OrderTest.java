package dev.keven.ecommerce.modules.order.domain;

import dev.keven.ecommerce.common.exception.CartItemNotFoundException;
import dev.keven.ecommerce.common.exception.OrderInvalidStatusException;
import dev.keven.ecommerce.common.exception.UserNullException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    void constructorShouldThrowWhenUserIdIsNull() {
        assertThrows(UserNullException.class, () -> new Order(null));
    }

    @Test
    void addItemShouldAddItemAndRecalculateTotal() {
        Order order = new Order(1L);

        order.addItem(10L, 2, new BigDecimal("7.50"));

        assertEquals(1, order.getItems().size());
        assertEquals(2, order.getTotalQuantity());
        assertEquals(new BigDecimal("15.00"), order.getTotalPrice());
    }

    @Test
    void addItemShouldIncreaseQuantityForSameProduct() {
        Order order = new Order(1L);

        order.addItem(10L, 1, new BigDecimal("5.00"));
        order.addItem(10L, 3, new BigDecimal("5.00"));

        assertEquals(1, order.getItems().size());
        assertEquals(4, order.getItems().getFirst().getQuantity());
        assertEquals(new BigDecimal("20.00"), order.getTotalPrice());
    }

    @Test
    void addItemShouldThrowWhenQuantityIsNotPositive() {
        Order order = new Order(1L);

        assertThrows(IllegalArgumentException.class, () -> order.addItem(10L, 0, new BigDecimal("5.00")));
        assertThrows(IllegalArgumentException.class, () -> order.addItem(10L, -1, new BigDecimal("5.00")));
    }

    @Test
    void removeItemShouldRemoveLineAndRecalculateTotal() {
        Order order = new Order(1L);
        order.addItem(10L, 2, new BigDecimal("4.00"));
        order.addItem(11L, 1, new BigDecimal("3.00"));

        order.removeItem(10L);

        assertEquals(1, order.getItems().size());
        assertEquals(11L, order.getItems().getFirst().getProductId());
        assertEquals(1, order.getTotalQuantity());
        assertEquals(new BigDecimal("3.00"), order.getTotalPrice());
    }

    @Test
    void removeItemShouldThrowWhenItemDoesNotExist() {
        Order order = new Order(1L);
        order.addItem(10L, 2, new BigDecimal("4.00"));

        assertThrows(CartItemNotFoundException.class, () -> order.removeItem(99L));
    }

    @Test
    void updateItemQuantityShouldChangeQuantityAndTotal() {
        Order order = new Order(1L);
        order.addItem(10L, 2, new BigDecimal("4.00"));

        order.updateItemQuantity(10L, 5, new BigDecimal("4.50"));

        assertEquals(1, order.getItems().size());
        assertEquals(5, order.getItems().getFirst().getQuantity());
        assertEquals(new BigDecimal("4.50"), order.getItems().getFirst().getPrice());
        assertEquals(new BigDecimal("22.50"), order.getTotalPrice());
    }

    @Test
    void updateItemQuantityShouldRemoveLineWhenQuantityIsZero() {
        Order order = new Order(1L);
        order.addItem(10L, 2, new BigDecimal("4.00"));
        order.addItem(11L, 1, new BigDecimal("3.00"));

        order.updateItemQuantity(10L, 0, new BigDecimal("4.00"));

        assertEquals(1, order.getItems().size());
        assertEquals(11L, order.getItems().getFirst().getProductId());
        assertEquals(new BigDecimal("3.00"), order.getTotalPrice());
    }

    @Test
    void updateItemQuantityShouldThrowWhenItemDoesNotExist() {
        Order order = new Order(1L);
        order.addItem(10L, 2, new BigDecimal("4.00"));

        assertThrows(CartItemNotFoundException.class, () -> order.updateItemQuantity(99L, 1, new BigDecimal("2.00")));
    }

    @Test
    void updateItemQuantityShouldThrowWhenQuantityIsNegative() {
        Order order = new Order(1L);
        order.addItem(10L, 2, new BigDecimal("4.00"));

        assertThrows(IllegalArgumentException.class, () -> order.updateItemQuantity(10L, -1, new BigDecimal("2.00")));
    }

    @Test
    void confirmShouldChangeStatusWhenOrderIsCreatedAndHasItems() {
        Order order = new Order(1L);
        order.addItem(10L, 1, new BigDecimal("9.90"));

        order.confirm();

        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    void confirmShouldThrowWhenOrderHasNoItems() {
        Order order = new Order(1L);

        assertThrows(IllegalArgumentException.class, order::confirm);
    }

    @Test
    void confirmShouldThrowWhenOrderIsNotCreated() {
        Order order = Order.rebuild(
                1L,
                1L,
                OrderStatus.CONFIRMED,
                new BigDecimal("9.90"),
                List.of(new OrderItem(1L, 10L, 1, new BigDecimal("9.90"))),
                LocalDateTime.now().minusDays(1)
        );

        assertThrows(OrderInvalidStatusException.class, order::confirm);
    }

    @Test
    void cancelShouldChangeStatusWhenOrderIsCreated() {
        Order order = new Order(1L);

        order.cancel();

        assertEquals(OrderStatus.CANCELED, order.getStatus());
    }

    @Test
    void cancelShouldThrowWhenOrderIsNotCreated() {
        Order order = Order.rebuild(
                1L,
                1L,
                OrderStatus.CONFIRMED,
                new BigDecimal("10.00"),
                List.of(new OrderItem(1L, 10L, 1, new BigDecimal("10.00"))),
                LocalDateTime.now().minusDays(1)
        );

        assertThrows(OrderInvalidStatusException.class, order::cancel);
    }

    @Test
    void editingMethodsShouldThrowWhenOrderIsNotCreated() {
        Order order = Order.rebuild(
                1L,
                1L,
                OrderStatus.CONFIRMED,
                new BigDecimal("10.00"),
                List.of(new OrderItem(1L, 10L, 1, new BigDecimal("10.00"))),
                LocalDateTime.now().minusDays(1)
        );

        assertThrows(OrderInvalidStatusException.class, () -> order.addItem(11L, 1, new BigDecimal("2.00")));
        assertThrows(OrderInvalidStatusException.class, () -> order.removeItem(10L));
        assertThrows(OrderInvalidStatusException.class, () -> order.updateItemQuantity(10L, 1, new BigDecimal("2.00")));
        assertThrows(OrderInvalidStatusException.class, order::validateIfCanBeDeleted);
    }
}
