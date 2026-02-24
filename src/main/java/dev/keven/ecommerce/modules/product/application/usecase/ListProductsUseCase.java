package dev.keven.ecommerce.modules.product.application.usecase;

import dev.keven.ecommerce.modules.product.application.command.ListProductsCommand;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.application.result.ListProductsResult;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;

public class ListProductsUseCase {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final ProductGateway productGateway;

    public ListProductsUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public ListProductsResult execute(ListProductsCommand command) {
        int page = command.page() == null ? DEFAULT_PAGE : Math.max(command.page(), 0);
        int requestedSize = command.size() == null ? DEFAULT_SIZE : command.size();

        if (requestedSize <= 0) {
            throw new IllegalArgumentException("size must be greater than zero");
        }

        int size = Math.min(requestedSize, MAX_PAGE_SIZE);

        ProductStatus status = command.includeInactive()
                ? command.status()
                : ProductStatus.ACTIVE;

        var products = productGateway.search(
                command.query(),
                status,
                page,
                size
        );

        return ListProductsResult.from(products);
    }
}
