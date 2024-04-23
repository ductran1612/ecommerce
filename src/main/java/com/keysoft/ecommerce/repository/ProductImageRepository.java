package com.keysoft.ecommerce.repository;

import com.keysoft.ecommerce.model.Product;
import com.keysoft.ecommerce.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProductAndEnable(Product product, Boolean enable);
}
