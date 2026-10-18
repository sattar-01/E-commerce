package ecommerce_backend.service;

import ecommerce_backend.entity.Product;
import ecommerce_backend.exception.ResourceNotFoundException;
import ecommerce_backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product updateProduct(Long id, Product productDetails) {

    Product product = productRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Product not found with id: " + id));

    product.setName(productDetails.getName());
    product.setDescription(productDetails.getDescription());
    product.setPrice(productDetails.getPrice());
    product.setStockQuantity(productDetails.getStockQuantity());

    return productRepository.save(product);
}

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }
}