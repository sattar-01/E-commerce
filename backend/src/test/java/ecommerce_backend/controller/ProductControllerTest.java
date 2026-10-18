package ecommerce_backend.controller;

import ecommerce_backend.entity.Product;
import ecommerce_backend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void shouldCreateProduct() throws Exception {

        Product product = new Product();
        product.setName("iPhone Case");
        product.setPrice(new BigDecimal("799.00"));
        product.setStockQuantity(10);

        when(productService.createProduct(any(Product.class)))
                .thenReturn(product);

        String requestBody = """
                {
                    "name": "iPhone Case",
                    "description": "Premium case",
                    "price": 799.00,
                    "stockQuantity": 10
                }
                """;

        mockMvc.perform(
                post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(status().isCreated());
    }
}