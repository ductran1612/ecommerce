package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.constant.TransactionStatusEnum;
import com.keysoft.ecommerce.dto.TransactionDTO;
import com.keysoft.ecommerce.model.Transaction;
import com.keysoft.ecommerce.model.TransactionDetail;
import com.keysoft.ecommerce.repository.TransactionDetailRepository;
import com.keysoft.ecommerce.repository.TransactionRepository;
import com.keysoft.ecommerce.service.TransactionService;
import com.keysoft.ecommerce.util.CodeHelper;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionDetailRepository transactionDetailRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<TransactionDTO> getAllTransactions(TransactionDTO transactionDTO) {
        log.info("service: get all transactions");
        Page<Transaction> page = transactionRepository.findAll(PageRequest.of(transactionDTO.getPage(), transactionDTO.getSize()));
        List<TransactionDTO> results = new ArrayList<>();
        for (Transaction transaction : page.getContent()) {
            TransactionDTO dto = modelMapper.map(transaction, TransactionDTO.class);
            results.add(dto);
        }
        return new PageImpl<>(results, PageRequest.of(transactionDTO.getPage(), transactionDTO.getSize()), page.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean save(TransactionDTO transaction) throws IllegalAccessException {
        log.info("service: save transaction");

        Transaction savedTransaction = modelMapper.map(transaction, Transaction.class);

        LocalDateTime modifiedDate = LocalDateTime.now();

        Set<TransactionDetail> details = savedTransaction.getTransactionDetails();
        savedTransaction.setTransactionDetails(Collections.emptySet());


        if (transaction.getId() != null) {
            Transaction oldTransaction = transactionRepository.findById(transaction.getId()).orElse(new Transaction());

            if (oldTransaction.getId() == null) {
                throw new IllegalAccessException("Thông tin giao dịch không tồn tại");
            } else {
                if (transaction.getDeletedDetails() != null && !transaction.getDeletedDetails().isEmpty()) {
                    transactionDetailRepository.deleteAllById(transaction.getDeletedDetails());
                }
            }
            savedTransaction.setCreatedDate(oldTransaction.getCreatedDate());
        } else {
            savedTransaction.setStatus(TransactionStatusEnum.PROGRESS.status);
            if(transaction.getCustomer() != null && !StringUtils.isBlank(transaction.getCustomer().getUsername())) {
                savedTransaction.setCustomer(customerRepository.findByUsernameAndEnableTrue(transaction.getCustomer().getUsername()));

                if(savedTransaction.getCustomer() == null || savedTransaction.getCustomer().getId() == null )
                    throw new IllegalAccessException("Thông tin khách hàng không tồn tại");
                else {
                    savedTransaction.setName(savedTransaction.getCustomer().getFullName());
                    savedTransaction.setPhone(savedTransaction.getCustomer().getPhone());
                    savedTransaction.setEmail(savedTransaction.getCustomer().getEmail());
                    savedTransaction.setAddress(savedTransaction.getCustomer().getAddress());
                }
            }

            if(StringUtils.isEmpty(savedTransaction.getCode()))
                savedTransaction.setCode(CodeHelper.spawnCode("T", LocalDateTime.now()));

            savedTransaction.setCreatedDate(modifiedDate);
        }

        if(savedTransaction.getBillInvoice() == null)
            savedTransaction.setBillInvoice(BigDecimal.valueOf(0));

        for (TransactionDetail detail : details) {
            if(detail.getProduct().getId() == null) {
                if(!StringUtils.isBlank(detail.getProduct().getCode())) {
                    detail.setProduct(productRepository.findByCodeIgnoreCase(detail.getProduct().getCode()).orElse(null));
                } else {
                    detail.setProduct(null);
                }
                if(detail.getProduct() == null || detail.getProduct().getId() == null) {
                    throw new IllegalAccessException("Thông tin sản phẩm không tồn tại");
                }
                detail.setSellDiscount(detail.getProduct().getDiscount());
                detail.setSellPrice(detail.getProduct().getPrice());
                detail.setTotal(detail.getSellPrice().subtract(detail.getSellDiscount()).multiply(BigDecimal.valueOf(detail.getQuantity())));

                savedTransaction.setBillInvoice(savedTransaction.getBillInvoice().add(detail.getTotal()));
            }

            detail.setTransaction(savedTransaction);
        }

        savedTransaction.setTransactionDetails(details);
        savedTransaction.setModifiedDate(modifiedDate);

        transaction = modelMapper.map(transactionRepository.save(savedTransaction), TransactionDTO.class);
        if(transaction.getId() == null)
            throw new IllegalAccessException("Lưu giao dịch thất bại");

        return transaction;
    }

    @Override
    public TransactionDTO get(Long id) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean delete(Long id) {
        return false;
    }
}
