package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.TransactionDTO;
import org.springframework.data.domain.Page;

public interface TransactionService {
    Page<TransactionDTO> getAllTransactions(TransactionDTO transactionDTO);
    boolean save(TransactionDTO transactionDTO) throws IllegalAccessException;
    TransactionDTO get(Long id);
    boolean delete(Long id);
}
