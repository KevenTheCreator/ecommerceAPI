package dev.keven.ecommerce.modules.cart.presentation.controller;

import dev.keven.ecommerce.modules.cart.application.usecase.AddItemToCartUseCase;
import dev.keven.ecommerce.modules.cart.application.usecase.CheckoutCartUseCase;
import dev.keven.ecommerce.modules.cart.application.usecase.GetCartUseCase;
import dev.keven.ecommerce.modules.cart.application.usecase.RemoveItemFromCartUseCase;
import dev.keven.ecommerce.modules.cart.application.usecase.UpdateCartItemUseCase;
import dev.keven.ecommerce.modules.cart.presentation.dto.request.AddItemToCartRequest;
import dev.keven.ecommerce.modules.cart.presentation.dto.request.UpdateCartItemRequest;
import dev.keven.ecommerce.modules.cart.presentation.dto.response.CartResponse;
import dev.keven.ecommerce.modules.cart.presentation.dto.response.CheckoutCartResponse;
import dev.keven.ecommerce.modules.cart.presentation.mapper.CartRequestMapper;
import dev.keven.ecommerce.modules.cart.presentation.mapper.CartResponseMapper;
import dev.keven.ecommerce.security.auth.AuthenticatedUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@PreAuthorize("hasAuthority('CUSTOMER')")
public class CartController {

    private final GetCartUseCase getCartUseCase;
    private final AddItemToCartUseCase addItemToCartUseCase;
    private final UpdateCartItemUseCase updateCartItemUseCase;
    private final RemoveItemFromCartUseCase removeItemFromCartUseCase;
    private final CheckoutCartUseCase checkoutCartUseCase;
    private final AuthenticatedUserService authenticatedUserService;

    public CartController(
            GetCartUseCase getCartUseCase,
            AddItemToCartUseCase addItemToCartUseCase,
            UpdateCartItemUseCase updateCartItemUseCase,
            RemoveItemFromCartUseCase removeItemFromCartUseCase,
            CheckoutCartUseCase checkoutCartUseCase,
            AuthenticatedUserService authenticatedUserService
    ) {
        this.getCartUseCase = getCartUseCase;
        this.addItemToCartUseCase = addItemToCartUseCase;
        this.updateCartItemUseCase = updateCartItemUseCase;
        this.removeItemFromCartUseCase = removeItemFromCartUseCase;
        this.checkoutCartUseCase = checkoutCartUseCase;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        Long authenticatedUserId = authenticatedUserService.getAuthenticatedUserId();
        var result = getCartUseCase.execute(CartRequestMapper.toGetCommand(authenticatedUserId));
        return ResponseEntity.status(HttpStatus.OK).body(CartResponseMapper.toResponse(result));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@RequestBody @Valid AddItemToCartRequest request) {
        Long authenticatedUserId = authenticatedUserService.getAuthenticatedUserId();
        var result = addItemToCartUseCase.execute(CartRequestMapper.toAddItemCommand(authenticatedUserId, request));
        return ResponseEntity.status(HttpStatus.OK).body(CartResponseMapper.toResponse(result));
    }

    @PatchMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateItem(@PathVariable Long productId, @RequestBody @Valid UpdateCartItemRequest request) {
        Long authenticatedUserId = authenticatedUserService.getAuthenticatedUserId();
        var result = updateCartItemUseCase.execute(
                CartRequestMapper.toUpdateItemCommand(authenticatedUserId, productId, request)
        );
        return ResponseEntity.status(HttpStatus.OK).body(CartResponseMapper.toResponse(result));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long productId) {
        Long authenticatedUserId = authenticatedUserService.getAuthenticatedUserId();
        removeItemFromCartUseCase.execute(
                CartRequestMapper.toRemoveItemCommand(authenticatedUserId, productId)
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutCartResponse> checkout() {
        Long authenticatedUserId = authenticatedUserService.getAuthenticatedUserId();
        var result = checkoutCartUseCase.execute(CartRequestMapper.toCheckoutCommand(authenticatedUserId));
        return ResponseEntity.status(HttpStatus.OK).body(CartResponseMapper.toResponse(result));
    }
}
