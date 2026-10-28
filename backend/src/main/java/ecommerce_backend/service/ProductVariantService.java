
package ecommerce_backend.service;

import ecommerce_backend.dto.ProductVariantRequest;
import ecommerce_backend.dto.ProductVariantResponse;
import ecommerce_backend.entity.Product;
import ecommerce_backend.entity.ProductVariant;
import ecommerce_backend.exception.ResourceNotFoundException;
import ecommerce_backend.repository.ProductRepository;
import ecommerce_backend.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductVariantService {

    private final ProductVariantRepository variantRepository;
    private final ProductRepository productRepository;

    public ProductVariantService(
            ProductVariantRepository variantRepository,
            ProductRepository productRepository) {
        this.variantRepository = variantRepository;
        this.productRepository = productRepository;
    }

    public ProductVariantResponse createVariant(
            Long productId,
            ProductVariantRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId));

        String sku = request.getSku().trim();
        String size = request.getSize().trim();
        String color = request.getColor().trim();

        if (variantRepository.existsBySkuIgnoreCase(sku)) {
            throw new IllegalArgumentException(
                    "SKU already exists: " + sku);
        }

        if (variantRepository
                .existsByProductIdAndSizeIgnoreCaseAndColorIgnoreCase(
                        productId, size, color)) {
            throw new IllegalArgumentException(
                    "This size and color variant already exists");
        }

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setSku(sku);
        variant.setSize(size);
        variant.setColor(color);
        variant.setPrice(request.getPrice());
        variant.setStockQuantity(request.getStockQuantity());
        variant.setActive(true);

        ProductVariant saved = variantRepository.save(variant);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductVariantResponse> getVariantsByProduct(
            Long productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(
                    "Product not found with id: " + productId);
        }

        return variantRepository.findByProductId(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

        @Transactional(readOnly = true)
        public ProductVariantResponse getVariantById(Long variantId) {

        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variant not found with id: " + variantId));

        return toResponse(variant);
        }

        public ProductVariantResponse updateVariant(
            Long variantId,
            ProductVariantRequest request) {

            ProductVariant variant = variantRepository.findById(variantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Variant not found with id: " + variantId));

            String sku = request.getSku().trim();
            String size = request.getSize().trim();
            String color = request.getColor().trim();

            variantRepository.findBySkuIgnoreCase(sku)
                    .filter(existing -> !existing.getId().equals(variantId))
                    .ifPresent(existing -> {
                            throw new IllegalArgumentException(
                                    "SKU already exists: " + sku);
                    });

            boolean duplicateVariant =
                    variantRepository
                            .existsByProductIdAndSizeIgnoreCaseAndColorIgnoreCase(
                                    variant.getProduct().getId(), size, color);

            if (duplicateVariant &&
                    (!variant.getSize().equalsIgnoreCase(size) ||
                    !variant.getColor().equalsIgnoreCase(color))) {
                    throw new IllegalArgumentException(
                            "This size and color variant already exists");
            }

            variant.setSku(sku);
            variant.setSize(size);
            variant.setColor(color);
            variant.setPrice(request.getPrice());
            variant.setStockQuantity(request.getStockQuantity());

            return toResponse(variantRepository.save(variant));
        }

        
        
        public void deleteVariant(Long variantId) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variant not found with id: " + variantId));

        variantRepository.delete(variant);
        }
        
        private ProductVariantResponse toResponse(ProductVariant variant) {
                return new ProductVariantResponse(
                        variant.getId(),
                        variant.getProduct().getId(),
                        variant.getProduct().getName(),
                        variant.getSku(),
                        variant.getSize(),
                        variant.getColor(),
                        variant.getPrice(),
                        variant.getStockQuantity(),
                        variant.getActive()
                );
        }
}
