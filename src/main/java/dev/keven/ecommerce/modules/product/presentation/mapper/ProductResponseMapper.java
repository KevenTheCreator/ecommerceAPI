package dev.keven.ecommerce.modules.product.presentation.mapper;

import dev.keven.ecommerce.modules.product.application.result.CreateProductResult;
import dev.keven.ecommerce.modules.product.application.result.GetProductByIdResult;
import dev.keven.ecommerce.modules.product.application.result.UpdateProductResult;
import dev.keven.ecommerce.modules.product.presentation.dto.response.CreateProductResponse;
import dev.keven.ecommerce.modules.product.presentation.dto.response.GetProductResponse;
import dev.keven.ecommerce.modules.product.presentation.dto.response.UpdateProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductResponseMapper {

    public static CreateProductResponse toResponse(CreateProductResult result) {
        return new CreateProductResponse(
                result.name(),
                result.description(),
                result.price(),
                result.stock(),
                result.status()
        );
    }

    public static UpdateProductResponse toResponse(UpdateProductResult result) {
        return new UpdateProductResponse(
                result.name(),
                result.description(),
                result.price(),
                result.stock(),
                result.status(),
                result.updated()
        );
    }

    public static GetProductResponse toResponse(GetProductByIdResult result) {
        return new GetProductResponse(
                result.id(),
                result.name(),
                result.description(),
                result.price(),
                result.stock(),
                result.status(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}
