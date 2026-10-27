
package ecommerce_backend.dto;

import java.math.BigDecimal;

public class ProductVariantResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String sku;
    private String size;
    private String color;
    private BigDecimal price;
    private Integer stockQuantity;
    private Boolean active;

    public ProductVariantResponse(
            Long id,
            Long productId,
            String productName,
            String sku,
            String size,
            String color,
            BigDecimal price,
            Integer stockQuantity,
            Boolean active) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.sku = sku;
        this.size = size;
        this.color = color;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getSku() {
        return sku;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public Boolean getActive() {
        return active;
    }
}
