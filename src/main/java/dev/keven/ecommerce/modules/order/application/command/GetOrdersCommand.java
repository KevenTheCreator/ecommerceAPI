package dev.keven.ecommerce.modules.order.application.command;

import dev.keven.ecommerce.modules.order.domain.OrderStatus;

import java.time.LocalDate;

public record GetOrdersCommand(
        Long userId,
        OrderStatus status,
        LocalDate from,
        LocalDate to,
        Integer page,
        Integer size
) {
}
