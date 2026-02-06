package dev.keven.ecommerce.modules.order.presentation.mapper;

import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderItem;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import dev.keven.ecommerce.modules.order.infrastructure.persistence.entity.OrderEntity;
import dev.keven.ecommerce.modules.order.infrastructure.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderEntityMapper {

    public static OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setUserId(order.getUserId());
        entity.setStatus(order.getStatus());
        entity.setTotalPrice(order.getTotalPrice());
        entity.setCreatedAt(order.getCreatedAt());

        List<OrderItemEntity> itemsEntities = new ArrayList<>();

        for (OrderItem item : order.getItems()) {
            OrderItemEntity itemEntity = new OrderItemEntity();
            itemEntity.setId(item.getId());
            itemEntity.setProductId(item.getProductId());
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setPrice(item.getPrice());
            itemEntity.setOrder(entity);

            itemsEntities.add(itemEntity);
        }
        entity.setItems(itemsEntities);
        return entity;
    }

    public static Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(i -> new OrderItem(
                        i.getId(),
                        i.getProductId(),
                        i.getQuantity(),
                        i.getPrice()
                ))
                .toList();
        return Order.rebuild(
                entity.getId(),
                entity.getUserId(),
                entity.getStatus(),
                entity.getTotalPrice(),
                items,
                entity.getCreatedAt()
        );
    }
}
