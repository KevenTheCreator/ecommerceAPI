package dev.keven.ecommerce.modules.product.presentation.controller;

import dev.keven.ecommerce.common.exception.ProductNotFoundException;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import dev.keven.ecommerce.modules.product.application.usecase.CreateProductUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.DeleteProductUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.GetProductByIdUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.ListProductsUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.UpdateProductUseCase;
import dev.keven.ecommerce.modules.product.presentation.dto.request.CreateProductRequest;
import dev.keven.ecommerce.modules.product.presentation.dto.request.UpdateProductRequest;
import dev.keven.ecommerce.modules.product.presentation.dto.response.CreateProductResponse;
import dev.keven.ecommerce.modules.product.presentation.dto.response.GetProductResponse;
import dev.keven.ecommerce.modules.product.presentation.dto.response.ListProductsResponse;
import dev.keven.ecommerce.modules.product.presentation.dto.response.UpdateProductResponse;
import dev.keven.ecommerce.modules.product.presentation.mapper.ProductRequestMapper;
import dev.keven.ecommerce.modules.product.presentation.mapper.ProductResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final ListProductsUseCase listProductsUseCase;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            DeleteProductUseCase deleteProductUseCase,
            UpdateProductUseCase updateProductUseCase,
            GetProductByIdUseCase getProductByIdUseCase,
            ListProductsUseCase listProductsUseCase
    ) {
        this.createProductUseCase = createProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.getProductByIdUseCase = getProductByIdUseCase;
        this.listProductsUseCase = listProductsUseCase;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<CreateProductResponse> create(@RequestBody @Valid CreateProductRequest request) {
        var result = createProductUseCase.execute(
                ProductRequestMapper.toCommand(request)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponseMapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<ListProductsResponse> listProducts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        boolean includeInactive = isAdminAuthenticated();
        var result = listProductsUseCase.execute(
                ProductRequestMapper.toListCommand(
                        q,
                        status,
                        page,
                        size,
                        includeInactive
                )
        );

        return ResponseEntity.status(HttpStatus.OK).body(ProductResponseMapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetProductResponse> getProductById(@PathVariable Long id) {
        var result = getProductByIdUseCase.execute(
                ProductRequestMapper.toGetByIdCommand(id)
        );

        if (!isAdminAuthenticated() && result.status() != ProductStatus.ACTIVE) {
            throw new ProductNotFoundException("Product not found");
        }

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

    private boolean isAdminAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> "ADMIN".equals(authority.getAuthority()));
    }
}
