package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.dto.CategoryDTO;
import com.keysoft.ecommerce.model.Category;
import com.keysoft.ecommerce.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        log.info("controller: get all categories");
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setPage(page);
        categoryDTO.setSize(size);
        Page<CategoryDTO> result = categoryService.getAllCategories(categoryDTO);
        if(result.isEmpty()){
            return ResponseEntity.ok("Chưa có danh mục nào!");
        } else {
            return ResponseEntity.ok(result);
        }
    }
//
//    @GetMapping("/add")
//    public ResponseEntity<?> addCategory() {
//        log.info("controller: add category form");
//        Map<String, Object> responseData = new HashMap<>();
//        responseData.put("category", new CategoryDTO());
//        return ResponseEntity.ok(responseData);
//    }

    @GetMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") String id) {
        log.info("controller: update category form, id = {}", id);
        Map<String, Object> responseData = new HashMap<>();
        try {
            CategoryDTO categoryDTO = categoryService.get(Long.valueOf(id));
            CategoryDTO parents = categoryService.get(categoryDTO.getParentsId());
            categoryDTO.setParentsCategory(parents);
            responseData.put("category", categoryService.get(Long.valueOf(id)));
            return ResponseEntity.ok(responseData);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable(value = "id") String id) {
        log.info("controller: delete category, id = {}", id);
        try{
            return ResponseEntity.ok(categoryService.delete(Long.valueOf(id)));
        }catch (NumberFormatException e){
            return ResponseEntity.badRequest().body("Xoá không thành công!");
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody CategoryDTO categoryDTO) {
        log.info("controller: save category");
        boolean isSaved = categoryService.save(categoryDTO);
        if(isSaved){
            return ResponseEntity.ok("Save successfully");
        }
        return ResponseEntity.badRequest().body("Save error");
    }

    @GetMapping("/listParents")
    public ResponseEntity<List<CategoryDTO>> getParentsCategories (@RequestParam (value = "keyword", required = false) String keyword) {
        log.info("controller: search parents categories, keyword: {}", keyword);
        List<CategoryDTO> results = categoryService.searchByKeyword (keyword, true);
        return ResponseEntity.ok(results.isEmpty() ? null : results);
    }

}
