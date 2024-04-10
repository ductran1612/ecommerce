package com.keysoft.ecommerce.repository;

import com.keysoft.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByName(String name);
    Optional<Product> findByCodeIgnoreCase(String code);
}
