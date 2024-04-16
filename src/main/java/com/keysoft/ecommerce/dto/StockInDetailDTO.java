package com.keysoft.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StockInDetailDTO {
    private Long id;
    private BigDecimal importPrice;
    private BigDecimal total;
    private ProductDTO product;
    private Integer quantity;

}
