package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.dto.TransactionDTO;
import com.keysoft.ecommerce.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
