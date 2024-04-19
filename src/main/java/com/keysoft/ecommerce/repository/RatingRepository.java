package com.keysoft.ecommerce.repository;

import com.keysoft.ecommerce.model.Product;
import com.keysoft.ecommerce.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long>, JpaSpecificationExecutor<Rating> {
    List<Rating> findByProduct(Product product);
}
