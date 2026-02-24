package dev.keven.ecommerce.modules.order.presentation.controller;

import dev.keven.ecommerce.modules.order.application.command.*;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import dev.keven.ecommerce.modules.order.application.usecase.*;
import dev.keven.ecommerce.modules.order.presentation.dto.response.*;
import dev.keven.ecommerce.modules.order.presentation.mapper.OrderResponseMapper;
import dev.keven.ecommerce.security.auth.AuthenticatedUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final CancelOrderUseCase cancelOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final GetOrdersUseCase getOrdersUseCase;
    private final AuthenticatedUserService authenticatedUserService;

    public OrderController(
            CancelOrderUseCase cancelOrderUseCase,
            GetOrderByIdUseCase getOrderByIdUseCase,
            GetOrdersUseCase getOrdersUseCase,
            AuthenticatedUserService authenticatedUserService
    ) {
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
        this.getOrdersUseCase = getOrdersUseCase;
        this.authenticatedUserService = authenticatedUserService;
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping
    public ResponseEntity<GetOrdersResponse> getOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        Long authenticatedUserId = authenticatedUserService.getAuthenticatedUserId();
        var result = getOrdersUseCase.execute(
                new GetOrdersCommand(authenticatedUserId, status, from, to, page, size)
        );

        return ResponseEntity.status(HttpStatus.OK).body(OrderResponseMapper.toResponse(result));
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
