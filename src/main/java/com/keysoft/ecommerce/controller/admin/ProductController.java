package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.constant.DefaultPageEnum;
import com.keysoft.ecommerce.dto.ProductDTO;
import com.keysoft.ecommerce.model.Category;
import com.keysoft.ecommerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/list")
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("controller: get all products");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPage(page);
        productDTO.setSize(size);

        Page<ProductDTO> result = productService.getAllProducts(productDTO);
        if(result.isEmpty()) {
            return new ResponseEntity<>("Chưa có sản phẩm nào!", HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    // Thêm mới sản phẩm
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO product) {
        try {
            productService.save(product);
            return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Cập nhật sản phẩm
    @PutMapping("/ucommitpdate/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDTO product) {
        try {
            product.setId(id);
            productService.save(product);
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Lấy thông tin sản phẩm để chỉnh sửa
    @GetMapping("/edit/{id}")
    public ResponseEntity<?> getProductForEdit(@PathVariable("id") Long id) {
        try {
            ProductDTO product = productService.findByID(id);
            List<Category> rootCategories = categoryService.getRootCategories();
            return ResponseEntity.ok(new ProductEditResponse(product, rootCategories));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
