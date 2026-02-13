package dev.keven.ecommerce.modules.order.presentation.mapper;

import dev.keven.ecommerce.modules.order.application.result.*;
import dev.keven.ecommerce.modules.order.presentation.dto.response.*;
import org.springframework.stereotype.Component;

@Component
public record OrderResponseMapper() {

    public static CreateOrderResponse toResponse(CreateOrderResult result) {
        return new CreateOrderResponse(
                result.orderId(),
                result.status(),
                result.totalPrice().toString()
        );
    }

    public static AddItemResponse toResponse(AddItemResult result) {
        return new AddItemResponse(
                result.orderId(),
                result.totalPrice(),
                result.totalQuantity()
        );
    }

    public static ConfirmOrderResponse toResponse(ConfirmOrderResult result) {
        return new ConfirmOrderResponse(
                result.orderId(),
                result.status(),
                result.totalItems(),
                result.totalPrice()
        );
    }

    public static CancelOrderResponse toResponse(CancelOrderResult result) {
        return new CancelOrderResponse(
                result.orderId(),
                result.status()
        );
    }

    public static RemoveItemResponse toResponse(RemoveItemFromOrderResult result) {
        return new RemoveItemResponse(
                result.orderId(),
                result.totalItems(),
                result.totalPrice()
        );
    }

    public static GetOrderResponse toResponse(GetOrderResult result) {
        return new GetOrderResponse(
                result.orderId(),
                result.userId(),
                result.status(),
                result.totalPrice(),
                result.items().stream()
                        .map(i -> new GetOrderResponse.OrderItemResponse(
                                i.productId(),
                                i.quantity(),
                                i.price(),
                                i.subtotal()
                        ))
                        .toList()
        );
    }
}
