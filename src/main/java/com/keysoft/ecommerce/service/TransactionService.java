package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.TransactionDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    Page<TransactionDTO> getAllTransactions(TransactionDTO transactionDTO, String keyword);
    boolean save(TransactionDTO transactionDTO) throws IllegalAccessException;
    TransactionDTO get(String id);
    boolean delete(String id);
    boolean cancel(String id) ;
    boolean confirm(String id) ;
    boolean received(String id);

    List<TransactionDTO> getTransactionByCustomer(String username);
}
