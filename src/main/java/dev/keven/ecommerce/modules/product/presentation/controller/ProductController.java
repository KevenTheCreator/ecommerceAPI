package dev.keven.ecommerce.modules.product.presentation.controller;

import dev.keven.ecommerce.modules.product.application.usecase.CreateProductUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.DeleteProductUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.GetProductByIdUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.UpdateProductUseCase;
import dev.keven.ecommerce.modules.product.presentation.dto.request.CreateProductRequest;
import dev.keven.ecommerce.modules.product.presentation.dto.request.UpdateProductRequest;
import dev.keven.ecommerce.modules.product.presentation.dto.response.CreateProductResponse;
import dev.keven.ecommerce.modules.product.presentation.dto.response.GetProductResponse;
import dev.keven.ecommerce.modules.product.presentation.dto.response.UpdateProductResponse;
import dev.keven.ecommerce.modules.product.presentation.mapper.ProductRequestMapper;
import dev.keven.ecommerce.modules.product.presentation.mapper.ProductResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;

    public ProductController(CreateProductUseCase createProductUseCase, DeleteProductUseCase deleteProductUseCase, UpdateProductUseCase updateProductUseCase, GetProductByIdUseCase getProductByIdUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.getProductByIdUseCase = getProductByIdUseCase;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<CreateProductResponse> create(@RequestBody @Valid CreateProductRequest request) {
        var result = createProductUseCase.execute(
                ProductRequestMapper.toCommand(request)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponseMapper.toResponse(result));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<GetProductResponse> getProductById(@PathVariable Long id) {
        var result = getProductByIdUseCase.execute(
                ProductRequestMapper.toGetByIdCommand(id)
        );
        return ResponseEntity.status(HttpStatus.OK).body(ProductResponseMapper.toResponse(result));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        deleteProductUseCase.execute(
                ProductRequestMapper.toDeleteCommand(id)
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateProductResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateProductRequest request) {
        var result = updateProductUseCase.execute(
                ProductRequestMapper.toCommand(id, request)
        );
        return ResponseEntity.status(HttpStatus.OK).body(ProductResponseMapper.toResponse(result));
    }
}
