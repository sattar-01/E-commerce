package ecommerce_backend.service;
import ecommerce_backend.entity.Category;
import ecommerce_backend.entity.Product;
import ecommerce_backend.exception.ResourceNotFoundException;
import ecommerce_backend.repository.CategoryRepository;
import ecommerce_backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product createProduct(Product product) {

        if (product.getCategory() == null
                || product.getCategory().getId() == null) {
            throw new IllegalArgumentException(
                    "Category ID is required");
        }

        Category category = findCategory(
                product.getCategory().getId());

        product.setCategory(category);

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));
    }

   
    public Product updateProduct(Long id, Product productDetails, Long categoryId) {
        Product product = productRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Product not found with id: " + id
                )
            );

        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID is required");
        }

        Category category = findCategory(categoryId);

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setCategory(category);

        return productRepository.save(product);
    }


    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));

        productRepository.delete(product);
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + categoryId));
    }

    public Product createProduct(Product product, Long categoryId) {
        Category category = findCategory(categoryId);
        product.setCategory(category);

        return productRepository.save(product);
    }
}
