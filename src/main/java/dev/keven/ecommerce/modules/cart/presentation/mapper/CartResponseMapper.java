package dev.keven.ecommerce.modules.cart.presentation.mapper;

import dev.keven.ecommerce.modules.cart.application.result.CartResult;
import dev.keven.ecommerce.modules.cart.application.result.CheckoutCartResult;
import dev.keven.ecommerce.modules.cart.presentation.dto.response.CartResponse;
import dev.keven.ecommerce.modules.cart.presentation.dto.response.CheckoutCartResponse;
import org.springframework.stereotype.Component;

@Component
public class CartResponseMapper {

    public static CartResponse toResponse(CartResult result) {
        return new CartResponse(
                result.cartId(),
                result.status(),
                result.totalPrice().toString(),
                result.totalQuantity(),
                result.items().stream().map(item ->
                        new CartResponse.CartItemResponse(
                                item.productId(),
                                item.quantity(),
                                item.unitPrice().toString(),
                                item.subtotal().toString(),
                                item.available(),
                                item.unavailableReason()
                        )
                ).toList()
        );
    }

    public static CheckoutCartResponse toResponse(CheckoutCartResult result) {
        return new CheckoutCartResponse(
                result.orderId(),
                result.status(),
                result.totalPrice()
        );
    }
}
