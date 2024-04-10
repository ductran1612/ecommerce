package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.dto.CategoryDTO;
import com.keysoft.ecommerce.model.Category;
import com.keysoft.ecommerce.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    @PostMapping ("/list")
    public ResponseEntity<?> getAllCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        log.info("controller: get all categories");
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setPage(page);
        categoryDTO.setSize(size);
        Page<CategoryDTO> result = categoryService.getAllCategories(categoryDTO);
        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy danh quyền nào!");
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllCategoriesNotPagination() {
        log.info("controller: get all categories not pagination");
        List<CategoryDTO> result = categoryService.getAllCategoriesNotPagination();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") String id) {
        log.info("controller: update category form, id = {}", id);
        Map<String, Object> responseData = new HashMap<>();
        try {
            CategoryDTO categoryDTO = categoryService.get(Long.valueOf(id));
            CategoryDTO parents = null;
            if(categoryDTO.getParentsId() != null) {
                parents = categoryService.get(categoryDTO.getParentsId());
            }

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
    public ResponseEntity<List<CategoryDTO>> getParentsCategories () {
        log.info("controller: get parents categories");
        List<CategoryDTO> results = categoryService.getParentsCategories();
        return ResponseEntity.ok(results.isEmpty() ? null : results);
    }

}
