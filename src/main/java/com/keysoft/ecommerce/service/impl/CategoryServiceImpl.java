package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.CategoryDTO;
import com.keysoft.ecommerce.dto.ProductDTO;
import com.keysoft.ecommerce.model.Category;
import com.keysoft.ecommerce.repository.CategoryRepository;
import com.keysoft.ecommerce.service.CategoryService;
import com.keysoft.ecommerce.specification.CategorySpecification;
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
import java.util.Objects;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategorySpecification categorySpecification;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CategoryDTO> getRootCategories() {
        log.info("service: get root category");
        List<Category> rootCategories = categoryRepository.findAll(categorySpecification.search(true));
        List<CategoryDTO> results = new ArrayList<>();

        for(Category item : rootCategories) {
            CategoryDTO dto = modelMapper.map(item, CategoryDTO.class);
            dto.setSubCategories(getSubCategories(item.getId()));
            results.add(dto);
        }
        return results;
    }

    public List<CategoryDTO> getSubCategories(Long id) {
        List<CategoryDTO> subDTO = new ArrayList<>();
        for (Category category : categoryRepository.findAllByParentsId(id)) {
            if(category.getEnable())
                subDTO.add(modelMapper.map(category, CategoryDTO.class));
        }
        return subDTO;
    }

    @Override
    public Page<CategoryDTO> getAllCategories(CategoryDTO categoryDTO) {
        log.info("service: get all categories");
        Page<Category> page = categoryRepository.findAll(categorySpecification.filter(), PageRequest.of(categoryDTO.getPage(), categoryDTO.getSize()));
        List<CategoryDTO> results = new ArrayList<>();

        for(Category item : page.getContent()) {
            CategoryDTO dto = modelMapper.map(item, CategoryDTO.class);
            if(dto.getParentsId() != null) {
                dto.setParentsCategory(modelMapper.map(categoryRepository.findById(dto.getParentsId()), CategoryDTO.class));
            }
            results.add(dto);
        }
        return new PageImpl<>(results, PageRequest.of(categoryDTO.getPage(), categoryDTO.getSize()), page.getTotalElements());
    }

    @Override
    public boolean save(CategoryDTO categoryDTO) {
        log.info("service: save category");
        Category category;
        if(checkNameUsed(categoryDTO))
            throw new IllegalStateException("Danh mục này đã tồn tại");
        if (categoryDTO.getId() != null) {
            category = categoryRepository.findById(categoryDTO.getId()).orElse(null);
            if (category == null) {
                throw new IllegalStateException("Không tìm thấy danh mục!");
            }
            category.setName(categoryDTO.getName());
            category.setParentsId(categoryDTO.getParentsId());
        } else {
            category = modelMapper.map(categoryDTO, Category.class);
            category.setEnable(true);
        }
        category.setCode(CodeHelper.spawnCodeFromName(category.getName()));
        return categoryRepository.save(category).getId() != null;
    }

    @Override
    public CategoryDTO get(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null)
            throw new IllegalStateException("Không tìm thấy danh mục sản phẩm");
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public boolean delete(Long id) {
        log.info("service: delete category id: {}", id);

        Category category = categoryRepository.findById(id).orElse(null);

        if(category == null){
            return false;
        }
        if(!category.getProducts().isEmpty())
            throw new IllegalStateException("Không thể xoá do danh mục đang chứa sản phẩm");
        category.setEnable(false);
        categoryRepository.save(category);
        return !categoryRepository.findById(id).orElse(new Category()).getEnable();
    }

    public boolean checkNameUsed(CategoryDTO criteria) {
        Category category = categoryRepository.findByName(criteria.getName()).orElse(null);

        if (category == null)
            return false;

        if (criteria.getId() == null) {
            return true;
        }

        return (!Objects.equals(category.getId(), criteria.getId()));
    }
}
