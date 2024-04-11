package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.ProductDTO;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<ProductDTO> getAllProducts(ProductDTO productDTO);
    boolean save(ProductDTO productDTO);

    ProductDTO get(String id);

    boolean delete(String id);
}
