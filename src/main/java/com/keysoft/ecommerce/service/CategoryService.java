package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getRootCategories();
    List<CategoryDTO> getAllCategories();
}
