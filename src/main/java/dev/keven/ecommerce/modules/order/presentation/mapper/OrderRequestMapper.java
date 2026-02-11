package dev.keven.ecommerce.modules.order.presentation.mapper;

import dev.keven.ecommerce.modules.order.application.command.AddItemToOrderCommand;
import dev.keven.ecommerce.modules.order.application.command.CreateOrderCommand;
import dev.keven.ecommerce.modules.order.application.command.CreateOrderItemCommand;
import dev.keven.ecommerce.modules.order.presentation.dto.request.AddItemRequest;
import dev.keven.ecommerce.modules.order.presentation.dto.request.CreateOrderItemRequest;
import dev.keven.ecommerce.modules.order.presentation.dto.request.CreateOrderRequest;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class OrderRequestMapper {

    public static CreateOrderCommand toCommand(CreateOrderRequest request){
        List<CreateOrderItemCommand> items = request.items().stream()
                .map(OrderRequestMapper::toItemCommand)
                .toList();
        return new CreateOrderCommand(request.userId(), items);
    }

    public static CreateOrderItemCommand toItemCommand(CreateOrderItemRequest item){
        return new CreateOrderItemCommand(
                item.productId(),
                item.quantity()
        );
    }

    public static AddItemToOrderCommand toAddItemCommand(Long orderId, AddItemRequest request){
        return new AddItemToOrderCommand(
                orderId,
                request.productId(),
                request.quantity()

        );
    }
}
