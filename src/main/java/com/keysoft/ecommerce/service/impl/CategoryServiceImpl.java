package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.CategoryDTO;
import com.keysoft.ecommerce.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    public List<CategoryDTO> getRootCategories() {
        return null;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return null;
    }
}
