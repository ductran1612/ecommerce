package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.constant.DefaultPageEnum;
import com.keysoft.ecommerce.dto.ProductDTO;
import com.keysoft.ecommerce.model.Category;
import com.keysoft.ecommerce.service.CategoryService;
import com.keysoft.ecommerce.service.ProductService;
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
@RequestMapping("/admin/product")
@Slf4j
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/list")
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("controller: get all products");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPage(page);
        productDTO.setSize(size);

        Page<ProductDTO> result = productService.getAllProducts(productDTO);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm nào!");
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/add")
    public ResponseEntity<?> addProduct() {
        log.info("controller: add product form");
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("product", new ProductDTO());
        responseData.put("rootCategories", categoryService.getRootCategories());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") String id) {
        log.info("controller: update product form, id = {}", id);
        Map<String, Object> responseData = new HashMap<>();
        try {
            responseData.put("product", productService.get(Long.valueOf(id)));
            responseData.put("rootCategories", categoryService.getRootCategories());
            return ResponseEntity.ok(responseData);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ProductDTO product) {
        boolean isSaved = productService.save(product);
        if(isSaved){
            return ResponseEntity.ok("Save successfully");
        }
        return ResponseEntity.badRequest().body("Save error");
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id){
        log.info("controller: delete product id: {}", id);
        try{
            return ResponseEntity.ok(productService.delete(Long.valueOf(id)));
        }catch (NumberFormatException e){
            return ResponseEntity.badRequest().body("Xoá không thành công!");
        }
    }

}
