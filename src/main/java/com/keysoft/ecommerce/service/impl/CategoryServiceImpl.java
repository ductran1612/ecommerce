package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.CategoryDTO;
import com.keysoft.ecommerce.dto.ProductDTO;
import com.keysoft.ecommerce.model.Category;
import com.keysoft.ecommerce.repository.CategoryRepository;
import com.keysoft.ecommerce.service.CategoryService;
import com.keysoft.ecommerce.util.CodeHelper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CategoryDTO> getRootCategories() {
        log.info("service: get root category");
        List<Category> rootCategories = categoryRepository.findByParentsIdIsNull();
        List<CategoryDTO> results = new ArrayList<>();

        for(Category item : rootCategories) {
            CategoryDTO dto = modelMapper.map(item, CategoryDTO.class);
            results.add(dto);
        }
        return results;
    }

    @Override
    public Page<CategoryDTO> getAllCategories(CategoryDTO categoryDTO) {
        log.info("service: get all categories");
        Page<Category> page = categoryRepository.findAll(PageRequest.of(categoryDTO.getPage(), categoryDTO.getSize()));
        List<CategoryDTO> results = new ArrayList<>();

        for(Category item : page.getContent()) {
            CategoryDTO dto = modelMapper.map(item, CategoryDTO.class);
            results.add(dto);
        }
        return new PageImpl<>(results, PageRequest.of(categoryDTO.getPage(), categoryDTO.getSize()), page.getTotalElements());
    }

    @Override
    public boolean save(CategoryDTO categoryDTO) {
        log.info("service: save category");
        Category category;
        if (categoryDTO.getId() != null) {
            category = categoryRepository.findById(categoryDTO.getId()).orElse(null);
            if (category == null) {
                throw new IllegalStateException("Không tìm thấy danh mục!");
            }
            category.setName(categoryDTO.getName());
            category.setCode(CodeHelper.spawnCodeFromName(categoryDTO.getName()));
            category.setParentsId(categoryDTO.getParentsId());
        } else {
            category = modelMapper.map(categoryDTO, Category.class);
            category.setEnable(true);
            category.setCode(CodeHelper.spawnCodeFromName(category.getName()));
        }
        return categoryRepository.save(category).getId() != null;
    }

    @Override
    public CategoryDTO get(Long id) {
        return modelMapper.map(categoryRepository.findById(id).orElse(null), CategoryDTO.class);
    }
}
