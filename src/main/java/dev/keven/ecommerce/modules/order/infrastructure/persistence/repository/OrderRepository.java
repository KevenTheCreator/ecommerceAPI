package dev.keven.ecommerce.modules.order.infrastructure.persistence.repository;

import dev.keven.ecommerce.modules.order.infrastructure.persistence.entity.OrderEntity;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByIdAndUserId(Long id, Long userId);
    Optional<OrderEntity> findByUserIdAndStatus(Long userId, OrderStatus status);
}
