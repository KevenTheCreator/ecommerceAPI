package dev.keven.ecommerce.modules.order.application.usecase;

import dev.keven.ecommerce.modules.order.application.command.GetOrdersCommand;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.application.result.GetOrdersResult;

public class GetOrdersUseCase {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final OrderGateway orderGateway;

    public GetOrdersUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public GetOrdersResult execute(GetOrdersCommand command) {
        if (command.from() != null && command.to() != null && command.from().isAfter(command.to())) {
            throw new IllegalArgumentException("from cannot be after to");
        }

        int page = command.page() == null ? DEFAULT_PAGE : Math.max(command.page(), 0);
        int requestedSize = command.size() == null ? DEFAULT_SIZE : command.size();

        if (requestedSize <= 0) {
            throw new IllegalArgumentException("size must be greater than zero");
        }

        int size = Math.min(requestedSize, MAX_PAGE_SIZE);

        var orders = orderGateway.searchByUser(
                command.userId(),
                command.status(),
                command.from(),
                command.to(),
                page,
                size
        );

        return GetOrdersResult.from(orders);
    }
}
