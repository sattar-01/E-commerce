package ecommerce_backend.service;

import ecommerce_backend.entity.Product;
import ecommerce_backend.exception.ResourceNotFoundException;
import ecommerce_backend.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProduct() {

        Product product = new Product();
        product.setName("iPhone Case");
        product.setPrice(new BigDecimal("799.00"));
        product.setStockQuantity(10);

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        assertEquals("iPhone Case", result.getName());
        assertEquals(new BigDecimal("799.00"), result.getPrice());

        verify(productRepository).save(product);
    }

    @Test
    void shouldGetProductById() {

        Product product = new Product();
        product.setName("iPhone Case");

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("iPhone Case", result.getName());

        verify(productRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getProductById(999L)
        );

        verify(productRepository).findById(999L);
    }

    @Test
    void shouldUpdateProduct() {

        Product existingProduct = new Product();
        existingProduct.setName("Old Case");
        existingProduct.setPrice(new BigDecimal("500.00"));
        existingProduct.setStockQuantity(5);

        Product updatedDetails = new Product();
        updatedDetails.setName("New Case");
        updatedDetails.setPrice(new BigDecimal("799.00"));
        updatedDetails.setStockQuantity(20);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(existingProduct));

        when(productRepository.save(existingProduct))
                .thenReturn(existingProduct);

        Product result =
                productService.updateProduct(1L, updatedDetails);

        assertEquals("New Case", result.getName());
        assertEquals(new BigDecimal("799.00"), result.getPrice());
        assertEquals(20, result.getStockQuantity());

        verify(productRepository).findById(1L);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void shouldDeleteProduct() {

        Product product = new Product();
        product.setName("iPhone Case");

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).delete(product);
    }
}