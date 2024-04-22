package com.keysoft.ecommerce.controller.shop;

import com.keysoft.ecommerce.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class ShopCustomerController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transaction/get/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable("id") String id){
        try {
            return ResponseEntity.ok(transactionService.get(id));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transaction/received")
    public ResponseEntity<?> received(@PathVariable("id") String id) {
        try{
            if(transactionService.received(id))
                return ResponseEntity.ok("Xác nhận thành công");
            return ResponseEntity.badRequest().body("Xác nhận không thành công");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
