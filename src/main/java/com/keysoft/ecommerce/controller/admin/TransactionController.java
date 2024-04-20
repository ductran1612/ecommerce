package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.dto.TransactionDTO;
import com.keysoft.ecommerce.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/transaction")
@Slf4j
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/list")
    public ResponseEntity<?> getAllTransactions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("controller: get all transactions");
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setPage(page);
        transactionDTO.setSize(size);

        Page<TransactionDTO> result = transactionService.getAllTransactions(transactionDTO);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy giao dịch nào!");
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable("id") String id) {
        log.info("controller: edit transaction");
        try{
            return ResponseEntity.ok(transactionService.get(id));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Không tìm thấy giao dịch phù hợp");
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody TransactionDTO transactionDTO) {
        log.info("controller: save transaction");
        try {
            boolean isSaved = transactionService.save(transactionDTO);
            if(isSaved)
                return ResponseEntity.ok().build();
            return ResponseEntity.badRequest().body("Lỗi khi lưu");
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body("Lỗi khi lưu");
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        log.info("controller: delete transaction");
        try{
            boolean isDeleted = transactionService.delete(id);
            if (isDeleted) {
                return ResponseEntity.ok("Xoá thành công");
            }
            return ResponseEntity.badRequest().body("Lỗi khi xoá");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xoá");
        }
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<?> confirm(@PathVariable("id") String id) {
        log.info("controller: confirm transaction");
        try{
            boolean isConfirmed = transactionService.confirm(id);
            if (isConfirmed) {
                return ResponseEntity.ok("Đã hoàn thành đơn hàng");
            }
            return ResponseEntity.badRequest().body("Lỗi không thể hoàn thành đơn hàng");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi không thể hoàn thành đơn hàng");
        }
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancel(@PathVariable("id") String id) {
        log.info("controller: cancel transaction");
        try{
            boolean isCanceled = transactionService.cancel(id);
            if (isCanceled) {
                return ResponseEntity.ok("Đã huỷ đơn hàng");
            }
            return ResponseEntity.badRequest().body("Lỗi không thể huỷ đơn hàng");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi không thể huỷ đơn hàng");
        }
    }

    @GetMapping("/order")
    public ResponseEntity<?> getOrderHistory(@RequestParam("username") String username) {
        try{
            return ResponseEntity.ok(transactionService.getTransactionByCustomer(username));
        }catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
