package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.ProductDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Page<ProductDTO> search(ProductDTO productDTO, String keyword);
    boolean save(ProductDTO productDTO);

    ProductDTO get(String id);

    boolean delete(String id);

    List<ProductDTO> searchByKeyword(String keyword);
}
