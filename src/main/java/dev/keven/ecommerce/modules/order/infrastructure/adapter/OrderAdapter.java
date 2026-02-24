package dev.keven.ecommerce.modules.order.infrastructure.adapter;

import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import dev.keven.ecommerce.modules.order.infrastructure.persistence.entity.OrderEntity;
import dev.keven.ecommerce.modules.order.infrastructure.persistence.repository.OrderRepository;
import dev.keven.ecommerce.modules.order.presentation.mapper.OrderEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class OrderAdapter implements OrderGateway {

    private static final LocalDateTime DEFAULT_FROM = LocalDateTime.of(1970, 1, 1, 0, 0);
    private static final LocalDateTime DEFAULT_TO = LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999_999_999);

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
    public Page<Order> searchByUser(
            Long userId,
            OrderStatus status,
            LocalDate from,
            LocalDate to,
            int page,
            int size
    ) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));

        LocalDateTime fromDateTime = from == null ? DEFAULT_FROM : from.atStartOfDay();
        LocalDateTime toDateTime = to == null
                ? DEFAULT_TO
                : to.plusDays(1).atStartOfDay().minusNanos(1);

        return repository.searchByUser(userId, status, fromDateTime, toDateTime, pageable)
                .map(OrderEntityMapper::toDomain);
    }
}
