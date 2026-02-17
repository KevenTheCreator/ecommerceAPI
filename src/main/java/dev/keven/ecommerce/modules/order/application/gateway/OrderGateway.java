package dev.keven.ecommerce.modules.order.application.gateway;

import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import java.util.Optional;

public interface OrderGateway {
    Order save(Order order);
    Optional<Order> findById(Long id);
    Optional<Order> findByIdAndUserId(Long id, Long userId);
    Optional<Order> findByUserIdAndStatus(Long userId, OrderStatus status);
}
