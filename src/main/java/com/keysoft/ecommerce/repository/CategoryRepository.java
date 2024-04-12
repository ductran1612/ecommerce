package com.keysoft.ecommerce.repository;

import com.keysoft.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    List<Category> findAllByParentsId(Long id);
    List<Category> findByParentsIdIsNull();

//    Optional<Category> findByCode(String code);

//    Optional<Category> findByName(String name);
}
