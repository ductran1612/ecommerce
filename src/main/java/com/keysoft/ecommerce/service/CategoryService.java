package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.CategoryDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getRootCategories();
    List<CategoryDTO> getAllCategoriesNotPagination();
    Page<CategoryDTO> getAllCategories(CategoryDTO categoryDTO);
    boolean save(CategoryDTO categoryDTO);

    CategoryDTO get(Long id);

    List<CategoryDTO> getParentsCategories();

    boolean delete(Long id);
}
