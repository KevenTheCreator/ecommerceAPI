package dev.keven.ecommerce.modules.order.domain;

import java.math.BigDecimal;

public class OrderItem {
    private Long id;
    private Long productId;
    private int quantity;
    private BigDecimal price;

    public OrderItem(Long id, Long productId, int quantity, BigDecimal price) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be greater than 0");
        if (price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("price cannot be negative");
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public void increaseQuantity(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be greater than 0");
        this.quantity += amount;
    }

    public void updateQuantityAndPrice(int quantity, BigDecimal price) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be greater than 0");
        if (price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("price cannot be negative");
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
