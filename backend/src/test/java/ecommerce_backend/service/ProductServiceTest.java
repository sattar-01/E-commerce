
package ecommerce_backend.service;

import ecommerce_backend.entity.Category;
import ecommerce_backend.entity.Product;
import ecommerce_backend.exception.ResourceNotFoundException;
import ecommerce_backend.repository.CategoryRepository;
import ecommerce_backend.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;

    @BeforeEach
        void setUp() {
        category = new Category();

        try {
                var idField = Category.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(category, 1L);
        } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
        }

        product = new Product();
        product.setName("Laptop");
        product.setDescription("Test laptop");
        product.setPrice(new BigDecimal("50000.00"));
        product.setStockQuantity(10);
        product.setCategory(category);
        }

    @Test
    void createProductShouldSaveProductWhenCategoryExists() {
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));
        when(productRepository.save(product))
                .thenReturn(product);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals(category, result.getCategory());

        verify(categoryRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void createProductShouldFailWhenCategoryIdIsMissing() {
        product.setCategory(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(product)
        );

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProductShouldFailWhenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.createProduct(product)
        );

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getAllProductsShouldReturnProducts() {
        when(productRepository.findAll())
                .thenReturn(List.of(product));

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());

        verify(productRepository).findAll();
    }

    @Test
    void getProductByIdShouldReturnProductWhenFound() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
    }

    @Test
    void getProductByIdShouldThrowWhenNotFound() {
        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getProductById(99L)
        );
    }

    @Test
    void deleteProductShouldDeleteWhenProductExists() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).delete(product);
    }

    @Test
    void deleteProductShouldThrowWhenProductDoesNotExist() {
        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.deleteProduct(99L)
        );

        verify(productRepository, never()).delete(any(Product.class));
    }

    
        @Test
        void updateProductShouldUpdateProductWhenProductAndCategoryExist() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product updatedDetails = new Product();
        updatedDetails.setName("Gaming Laptop");
        updatedDetails.setDescription("Updated laptop");
        updatedDetails.setPrice(new BigDecimal("75000.00"));
        updatedDetails.setStockQuantity(5);
        updatedDetails.setCategory(category);

        
        Product result = productService.updateProduct(1L, updatedDetails, 1L);  

        assertEquals("Gaming Laptop", result.getName());
        assertEquals("Updated laptop", result.getDescription());
        assertEquals(new BigDecimal("75000.00"), result.getPrice());
        assertEquals(5, result.getStockQuantity());
        assertEquals(category, result.getCategory());

        verify(productRepository).save(product);
        }

        @Test
        void updateProductShouldThrowWhenProductDoesNotExist() {
        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        Product updatedDetails = new Product();

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.updateProduct(99L, updatedDetails, 1L)     
        );

        verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        void updateProductShouldThrowWhenCategoryDoesNotExist() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(categoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        Product updatedDetails = new Product();
        updatedDetails.setName("Gaming Laptop");
        updatedDetails.setDescription("Updated laptop");
        updatedDetails.setPrice(new BigDecimal("75000.00"));
        updatedDetails.setStockQuantity(5);

        Category missingCategory = new Category();

        try {
                var idField = Category.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(missingCategory, 99L);
        } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
        }

        updatedDetails.setCategory(missingCategory);

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.updateProduct(1L, updatedDetails, 99L)     
        );

        verify(productRepository, never()).save(any(Product.class));
        }

}
