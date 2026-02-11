package dev.keven.ecommerce.modules.order.domain;

import dev.keven.ecommerce.common.exception.OrderInvalidStatusException;
import dev.keven.ecommerce.common.exception.UserNullException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private List<OrderItem> items;
    private LocalDateTime createdAt;

    public Order(Long userId) {
        if (userId == null) throw new UserNullException("userId is null");
        this.userId = userId;
        this.status = OrderStatus.CREATED;
        this.items = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
    }

    public Order() {}

    public Order(Long id, Long userId, OrderStatus status, BigDecimal totalPrice, List<OrderItem> items) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.items = new ArrayList<>(items);
        this.totalPrice = totalPrice;
        this.createdAt = LocalDateTime.now();
    }

    public void addItem(Long productId, int quantity, BigDecimal price) {
        ensureEditable();

        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }

        OrderItem existent = findItemByProductId(productId);

        if (existent != null) {
            existent.increaseQuantity(quantity);
        } else {
            items.add(new OrderItem(null, productId, quantity, price));
        }
        recalculateTotal();
    }

    public int getTotalQuantity() {
        return items.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    public void removeItem(Long productId) {
        ensureEditable();
        items.removeIf(item -> item.getProductId().equals(productId));
        recalculateTotal();
    }

    public void confirm() {
        if (status != OrderStatus.CREATED) throw new OrderInvalidStatusException("only orders with status CREATED can be confirmed");
        if (items.isEmpty()) throw new IllegalArgumentException("cannot confirm an empty order");
        this.status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        if (status != OrderStatus.CREATED) throw new OrderInvalidStatusException("only orders with status CREATED can be canceled");
        this.status = OrderStatus.CANCELED;
    }

    private void recalculateTotal() {
        this.totalPrice = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void ensureEditable() {
        if (status != OrderStatus.CREATED) {
            throw new OrderInvalidStatusException("only orders in CREATED status can be modified");
        }
    }

    private OrderItem findItemByProductId(Long productId) {
        return items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public void validateIfCanBeDeleted() {
        if (this.status != OrderStatus.CREATED) {
            throw new OrderInvalidStatusException("only orders with status CREATED can be deleted");
        }
    }


    public static Order rebuild(
            Long id,
            Long userId,
            OrderStatus status,
            BigDecimal totalPrice,
            List<OrderItem> items,
            LocalDateTime createdAt
    ) {
        Order order = new Order(userId);
        order.id = id;
        order.status = status;
        order.totalPrice = totalPrice;
        order.items = new ArrayList<>(items);
        order.createdAt = createdAt;

        return order;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    List<OrderItem> getItemInternal() { return items; }
}
