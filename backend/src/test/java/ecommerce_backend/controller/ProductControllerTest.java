
package ecommerce_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecommerce_backend.dto.ProductRequest;
import ecommerce_backend.entity.Category;
import ecommerce_backend.entity.Product;
import ecommerce_backend.exception.GlobalExceptionHandler;
import ecommerce_backend.exception.ResourceNotFoundException;
import ecommerce_backend.service.ProductService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = ProductController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@Import(GlobalExceptionHandler.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private Product createProduct() {
        Category category = new Category();
        category.setName("Electronics");

        Product product = new Product();
        product.setName("Laptop");
        product.setDescription("Test laptop");
        product.setPrice(new BigDecimal("50000.00"));
        product.setStockQuantity(10);
        product.setActive(true);
        product.setCategory(category);

        return product;
    }

    private ProductRequest createRequest() {
        ProductRequest request = new ProductRequest();
        request.setName("Laptop");
        request.setDescription("Test laptop");
        request.setPrice(new BigDecimal("50000.00"));
        request.setStockQuantity(10);
        request.setCategoryId(1L);

        return request;
    }

    @Test
    void getAllProductsShouldReturnOk() throws Exception {
        when(productService.getAllProducts())
                .thenReturn(List.of(createProduct()));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].categoryName").value("Electronics"));
    }

    @Test
    void getProductByIdShouldReturnProduct() throws Exception {
        when(productService.getProductById(1L))
                .thenReturn(createProduct());

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void getProductByIdShouldReturnNotFound() throws Exception {
        when(productService.getProductById(99L))
                .thenThrow(new ResourceNotFoundException(
                        "Product not found with id: 99"));

        mockMvc.perform(get("/api/v1/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProductShouldReturnCreated() throws Exception {
        when(productService.createProduct(
                any(Product.class), eq(1L)))
                .thenReturn(createProduct());

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void createProductShouldRejectBlankName() throws Exception {
        ProductRequest request = createRequest();
        request.setName(" ");

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .createProduct(any(Product.class), any(Long.class));
    }

    @Test
    void updateProductShouldReturnOk() throws Exception {      
        when(productService.updateProduct(eq(1L), any(Product.class), eq(1L)))
        .thenReturn(createProduct());        

        mockMvc.perform(put("/api/v1/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void deleteProductShouldReturnNoContent() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }
}
