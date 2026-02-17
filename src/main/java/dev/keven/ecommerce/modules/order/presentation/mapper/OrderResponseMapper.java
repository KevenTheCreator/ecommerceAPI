package dev.keven.ecommerce.modules.order.presentation.mapper;

import dev.keven.ecommerce.modules.order.application.result.CancelOrderResult;
import dev.keven.ecommerce.modules.order.application.result.GetOrderResult;
import dev.keven.ecommerce.modules.order.presentation.dto.response.CancelOrderResponse;
import dev.keven.ecommerce.modules.order.presentation.dto.response.GetOrderResponse;
import org.springframework.stereotype.Component;

@Component
public record OrderResponseMapper() {

    public static CancelOrderResponse toResponse(CancelOrderResult result) {
        return new CancelOrderResponse(
                result.orderId(),
                result.status()
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
