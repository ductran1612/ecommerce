package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.StockInDTO;
import org.springframework.data.domain.Page;

public interface StockInService {
    Page<StockInDTO> getAllStockIns(StockInDTO stockInDTO, String keyword);
    StockInDTO get(String id);
    Boolean save(StockInDTO stockInDTO) throws IllegalAccessException;
}
