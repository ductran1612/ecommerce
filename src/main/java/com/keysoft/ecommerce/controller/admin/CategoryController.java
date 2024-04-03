package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.dto.CategoryDTO;
import com.keysoft.ecommerce.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

//    @GetMapping("/list")
//    public ResponseEntity<?> getAllCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
//        log.info("controller: get all categories");
//        CategoryDTO categoryDTO = new CategoryDTO();
//        categoryDTO.setPage(page);
//        categoryDTO.setSize(size);
//    }

}
