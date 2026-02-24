package dev.keven.ecommerce.modules.order.presentation.mapper;

import dev.keven.ecommerce.modules.order.application.result.CancelOrderResult;
import dev.keven.ecommerce.modules.order.application.result.GetOrderResult;
import dev.keven.ecommerce.modules.order.application.result.GetOrdersResult;
import dev.keven.ecommerce.modules.order.presentation.dto.response.CancelOrderResponse;
import dev.keven.ecommerce.modules.order.presentation.dto.response.GetOrderResponse;
import dev.keven.ecommerce.modules.order.presentation.dto.response.GetOrdersResponse;
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

    public static GetOrdersResponse toResponse(GetOrdersResult result) {
        return new GetOrdersResponse(
                result.content().stream()
                        .map(order -> new GetOrdersResponse.OrderSummaryResponse(
                                order.orderId(),
                                order.status(),
                                order.totalPrice(),
                                order.createdAt()
                        ))
                        .toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages(),
                result.first(),
                result.last()
        );
    }
}
