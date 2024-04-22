package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.dto.CustomerDTO;
import com.keysoft.ecommerce.dto.ProductDTO;
import com.keysoft.ecommerce.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/customer")
@Slf4j
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @PostMapping("/list")
    public ResponseEntity<?> getAllCustomer(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("controller: get all customers");
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setPage(page);
        customerDTO.setSize(size);
        Page<CustomerDTO> result = customerService.getAllCustomers(customerDTO);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy khách hàng nào!");
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable("id") String id) {
        log.info("controller: update customer form, id = {}", id);
        try {
            return ResponseEntity.ok(customerService.get(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody CustomerDTO customer) {
        try{
            boolean isSaved = customerService.save(customer);
            if(isSaved){
                return ResponseEntity.ok("Lưu thành công");
            }
            return ResponseEntity.badRequest().body("Lỗi khi lưu");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") String id){
        log.info("controller: delete customer id: {}", id);
        try{
            if(customerService.delete(id))
                return ResponseEntity.ok("Xoá thành công");
            return ResponseEntity.badRequest().body("Xoá không thành công!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/api/list")
    public ResponseEntity<List<CustomerDTO>> getList(@RequestParam(value = "keyword", required = false) String keyword) {
        log.info("controller: search list customer by keyword, keyword: {}", keyword);
        List<CustomerDTO> results = customerService.searchByKeyword(keyword);
        return ResponseEntity.ok(results.isEmpty() ? null : results);
    }
}
