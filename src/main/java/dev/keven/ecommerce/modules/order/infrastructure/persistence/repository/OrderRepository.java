package dev.keven.ecommerce.modules.order.infrastructure.persistence.repository;

import dev.keven.ecommerce.modules.order.infrastructure.persistence.entity.OrderEntity;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByIdAndUserId(Long id, Long userId);
    Optional<OrderEntity> findByUserIdAndStatus(Long userId, OrderStatus status);

    @Query("""
            SELECT o
            FROM OrderEntity o
            WHERE o.userId = :userId
              AND (:status IS NULL OR o.status = :status)
              AND o.createdAt BETWEEN :from AND :to
            """)
    Page<OrderEntity> searchByUser(
            @Param("userId") Long userId,
            @Param("status") OrderStatus status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable
    );
}
