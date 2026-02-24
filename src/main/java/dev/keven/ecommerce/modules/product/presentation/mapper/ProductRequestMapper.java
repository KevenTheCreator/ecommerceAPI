package dev.keven.ecommerce.modules.product.presentation.mapper;

import dev.keven.ecommerce.modules.product.application.command.CreateProductCommand;
import dev.keven.ecommerce.modules.product.application.command.DeleteProductCommand;
import dev.keven.ecommerce.modules.product.application.command.GetProductByIdCommand;
import dev.keven.ecommerce.modules.product.application.command.ListProductsCommand;
import dev.keven.ecommerce.modules.product.application.command.UpdateProductCommand;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import dev.keven.ecommerce.modules.product.presentation.dto.request.CreateProductRequest;
import dev.keven.ecommerce.modules.product.presentation.dto.request.UpdateProductRequest;
import org.springframework.stereotype.Component;

@Component
public class ProductRequestMapper {

    public static CreateProductCommand toCommand(CreateProductRequest request) {
        return new CreateProductCommand(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.status()
        );
    }

    public static UpdateProductCommand toCommand(Long productId, UpdateProductRequest request) {
        return new UpdateProductCommand(
                productId,
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.status()
        );
    }

    public static GetProductByIdCommand toGetByIdCommand(Long productId) {
        return new GetProductByIdCommand(productId);
    }

    public static DeleteProductCommand toDeleteCommand(Long productId) {
        return new DeleteProductCommand(productId);
    }

    public static ListProductsCommand toListCommand(
            String query,
            ProductStatus status,
            Integer page,
            Integer size,
            boolean includeInactive
    ) {
        return new ListProductsCommand(
                query,
                status,
                page,
                size,
                includeInactive
        );
    }
}
