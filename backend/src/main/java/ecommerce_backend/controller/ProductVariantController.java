
package ecommerce_backend.controller;

import ecommerce_backend.dto.ProductVariantRequest;
import ecommerce_backend.dto.ProductVariantResponse;
import ecommerce_backend.service.ProductVariantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductVariantController {

    private static final Logger log = LoggerFactory.getLogger(ProductVariantController.class);
    private final ProductVariantService variantService;
    public ProductVariantController(
            ProductVariantService variantService) {
        this.variantService = variantService;
    }

    @PostMapping("/products/{productId}/variants")
    public ResponseEntity<ProductVariantResponse> createVariant(
            @PathVariable Long productId,
            @Valid @RequestBody ProductVariantRequest request) {

        ProductVariantResponse response =
                variantService.createVariant(productId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/products/{productId}/variants")
    public ResponseEntity<List<ProductVariantResponse>> getVariants(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                variantService.getVariantsByProduct(productId));
    }

    @GetMapping("/variants/{variantId}")
    public ResponseEntity<ProductVariantResponse> getVariantById(
            @PathVariable Long variantId) {

        return ResponseEntity.ok(
                variantService.getVariantById(variantId)
        );
    }

    @PutMapping("/variants/{variantId}")
    public ResponseEntity<ProductVariantResponse> updateVariant(
            @PathVariable Long variantId,
            @Valid @RequestBody ProductVariantRequest request) {

        log.info("PUT request received for variantId={}", variantId);

        ProductVariantResponse response =
                variantService.updateVariant(variantId, request);

        log.info("PUT request completed successfully for variantId={}", variantId);

        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/variants/{variantId}")
    public ResponseEntity<Void> deleteVariant(
            @PathVariable Long variantId) {

        variantService.deleteVariant(variantId);
        return ResponseEntity.noContent().build();
    }
}
