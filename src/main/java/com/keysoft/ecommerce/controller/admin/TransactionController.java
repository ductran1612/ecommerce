package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/transaction")
@Slf4j
public class TransactionController {
    @PostMapping("/list")
    public ResponseEntity<?> getAllTransactions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("controller: get all transactions");
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setPage(page);
        transactionDTO.setSize(size);

        Page<TransactionDTO> result =
    }
}
