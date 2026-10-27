
package ecommerce_backend.repository;

import ecommerce_backend.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository
        extends JpaRepository<ProductVariant, Long> {

    List<ProductVariant> findByProductId(Long productId);

    List<ProductVariant> findByProductIdAndActiveTrue(Long productId);

    Optional<ProductVariant> findBySkuIgnoreCase(String sku);

    boolean existsBySkuIgnoreCase(String sku);

    boolean existsByProductIdAndSizeIgnoreCaseAndColorIgnoreCase(
            Long productId,
            String size,
            String color
    );
}
