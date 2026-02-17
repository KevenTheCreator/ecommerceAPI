package dev.keven.ecommerce.modules.order.infrastructure.adapter;

import dev.keven.ecommerce.common.exception.OrderNotFoundException;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import dev.keven.ecommerce.modules.order.infrastructure.persistence.entity.OrderEntity;
import dev.keven.ecommerce.modules.order.infrastructure.persistence.repository.OrderRepository;
import dev.keven.ecommerce.modules.order.presentation.mapper.OrderEntityMapper;
import java.util.Optional;

public class OrderAdapter implements OrderGateway {

    private final OrderRepository repository;
    private final OrderEntityMapper entityMapper;

    public OrderAdapter(OrderRepository repository,  OrderEntityMapper entityMapper) {
        this.repository = repository;
        this.entityMapper = entityMapper;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = entityMapper.toEntity(order);
        OrderEntity saved = repository.save(entity);
        return entityMapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return repository.findById(id)
                .map(OrderEntityMapper::toDomain);
    }

    @Override
    public Optional<Order> findByIdAndUserId(Long id, Long userId) {
        return repository.findByIdAndUserId(id, userId)
                .map(OrderEntityMapper::toDomain);
    }

    @Override
    public Optional<Order> findByUserIdAndStatus(Long userId, OrderStatus status) {
        return repository.findByUserIdAndStatus(userId, status)
                .map(OrderEntityMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        var existent = repository.findById(id)
                        .orElseThrow(() -> new OrderNotFoundException("order not found"));
        repository.deleteById(existent.getId());
    }
}
