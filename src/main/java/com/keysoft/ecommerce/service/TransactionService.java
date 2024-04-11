package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.TransactionDTO;
import org.springframework.data.domain.Page;

public interface TransactionService {
    Page<TransactionDTO> getAllTransactions(TransactionDTO transactionDTO);
    boolean save(TransactionDTO transactionDTO) throws IllegalAccessException;
    TransactionDTO get(String id);
    boolean delete(String id);
    boolean cancel(String id) throws IllegalAccessException;
    boolean confirm(String id) throws IllegalAccessException;

}
