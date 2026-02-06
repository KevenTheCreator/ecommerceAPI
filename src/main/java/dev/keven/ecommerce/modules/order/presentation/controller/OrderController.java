package dev.keven.ecommerce.modules.order.presentation.controller;

import dev.keven.ecommerce.modules.order.application.command.*;
import dev.keven.ecommerce.modules.order.application.usecase.*;
import dev.keven.ecommerce.modules.order.presentation.dto.request.*;
import dev.keven.ecommerce.modules.order.presentation.dto.response.*;
import dev.keven.ecommerce.modules.order.presentation.mapper.OrderRequestMapper;
import dev.keven.ecommerce.modules.order.presentation.mapper.OrderResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final AddItemToOrderUseCase addItemToOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final CreateOrderUseCase createOrderUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;
    private final RemoveItemFromOrderUseCase removeItemFromOrderUseCase;

    public OrderController(AddItemToOrderUseCase addItemToOrderUseCase, CancelOrderUseCase cancelOrderUseCase, ConfirmOrderUseCase confirmOrderUseCase, CreateOrderUseCase createOrderUseCase, DeleteOrderUseCase deleteOrderUseCase, RemoveItemFromOrderUseCase removeItemFromOrderUseCase) {
        this.addItemToOrderUseCase = addItemToOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.confirmOrderUseCase = confirmOrderUseCase;
        this.createOrderUseCase = createOrderUseCase;
        this.deleteOrderUseCase = deleteOrderUseCase;
        this.removeItemFromOrderUseCase = removeItemFromOrderUseCase;
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        var result = createOrderUseCase.execute(
                OrderRequestMapper.toCommand(request)
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrderResponseMapper.toResponse(result));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/{id}/items")
    public ResponseEntity<AddItemResponse> addItemToOrder(@PathVariable Long id, @RequestBody @Valid AddItemRequest request) {
        var result = addItemToOrderUseCase.execute(
                OrderRequestMapper.toAddItemCommand(id, request)
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(OrderResponseMapper.toResponse(result));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping("/{id}/items/{productId}")
    public ResponseEntity<RemoveItemResponse> removeItemFromOrder(@PathVariable Long id, @PathVariable Long productId) {
        var result = removeItemFromOrderUseCase.execute(
                new RemoveItemFromOrderCommand(
                        id,
                        productId
                )
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(OrderResponseMapper.toResponse(result));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/{id}/confirm")
    public ResponseEntity<ConfirmOrderResponse> confirmOrder(@PathVariable Long id) {
        var result = confirmOrderUseCase.execute(
                new ConfirmOrderCommand(id)
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(OrderResponseMapper.toResponse(result));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<CancelOrderResponse> cancelOrder(@PathVariable Long id) {
        var result = cancelOrderUseCase.execute(
              new CancelOrderCommand(id)
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(OrderResponseMapper.toResponse(result));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        deleteOrderUseCase.execute(new DeleteOrderCommand(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
