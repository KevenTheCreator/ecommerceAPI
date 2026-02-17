package dev.keven.ecommerce.modules.order.presentation.controller;

import dev.keven.ecommerce.modules.order.application.command.*;
import dev.keven.ecommerce.modules.order.application.usecase.*;
import dev.keven.ecommerce.modules.order.presentation.dto.response.*;
import dev.keven.ecommerce.modules.order.presentation.mapper.OrderResponseMapper;
import dev.keven.ecommerce.security.auth.AuthenticatedUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final CancelOrderUseCase cancelOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final AuthenticatedUserService authenticatedUserService;

    public OrderController(CancelOrderUseCase cancelOrderUseCase, GetOrderByIdUseCase getOrderByIdUseCase, AuthenticatedUserService authenticatedUserService) {
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
        this.authenticatedUserService = authenticatedUserService;
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<CancelOrderResponse> cancelOrder(@PathVariable Long id) {
        Long authenticatedUserId = authenticatedUserService.getAuthenticatedUserId();
        var result = cancelOrderUseCase.execute(
              new CancelOrderCommand(authenticatedUserId, id)
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(OrderResponseMapper.toResponse(result));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<GetOrderResponse> getOrderById(@PathVariable Long id) {
        Long authenticatedUserId = authenticatedUserService.getAuthenticatedUserId();
        var result = getOrderByIdUseCase.execute(
                new GetOrderByIdCommand(authenticatedUserId, id)
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(OrderResponseMapper.toResponse(result));
    }
}
