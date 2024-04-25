package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.constant.ProductStatusEnum;
import com.keysoft.ecommerce.dto.ProductDTO;
import com.keysoft.ecommerce.dto.StockInDTO;
import com.keysoft.ecommerce.dto.StockInDetailDTO;
import com.keysoft.ecommerce.model.Product;
import com.keysoft.ecommerce.model.StockIn;
import com.keysoft.ecommerce.model.StockInDetail;
import com.keysoft.ecommerce.repository.ProductRepository;
import com.keysoft.ecommerce.repository.StockInDetailRepository;
import com.keysoft.ecommerce.repository.StockInRepository;
import com.keysoft.ecommerce.service.StockInService;
import com.keysoft.ecommerce.specification.StockInSpecification;
import com.keysoft.ecommerce.util.CodeHelper;
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
import java.util.*;

@Service
@Slf4j
public class StockInServiceImpl implements StockInService {
    @Autowired
    private StockInRepository stockInRepository;
    @Autowired
    private StockInDetailRepository stockInDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockInSpecification stockInSpecification;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<StockInDTO> getAllStockIns(StockInDTO stockInDTO, String keyword) {
        log.info("service: get all stock in");
        Page<StockIn> page = stockInRepository.findAll(stockInSpecification.filter(keyword),
                PageRequest.of(stockInDTO.getPage(), stockInDTO.getSize()));
        List<StockInDTO> results = new ArrayList<>();
        for (StockIn item : page.getContent()) {
            results.add(modelMapper.map(item, StockInDTO.class));
        }
        return new PageImpl<>(results, PageRequest.of(stockInDTO.getPage(), stockInDTO.getSize()), page.getTotalElements());
    }

    @Override
    public StockInDTO get(String id) {
        StockInDTO stockInDTO;
        try {
            stockInDTO = modelMapper.map(stockInRepository.findById(Long.valueOf(id)).orElse(null), StockInDTO.class);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id không hợp lệ");
        }
        return stockInDTO;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public Boolean save(StockInDTO stockInDTO) {
        log.info("sevice: save stock in");
        StockIn savedStockIn = modelMapper.map(stockInDTO, StockIn.class);
        Set<StockInDetail> details = savedStockIn.getStockInDetails();
        savedStockIn.setStockInDetails(Collections.emptySet());

        if (stockInDTO.getId() != null) {
            StockIn oldStock = stockInRepository.findById(stockInDTO.getId()).orElse(null);
            if (oldStock == null)
                throw new IllegalStateException("Phiếu nhập kho không tồn tại");
            if (stockInDTO.getDeletedDetails() != null && !stockInDTO.getDeletedDetails().isEmpty())
                stockInDetailRepository.deleteAllById(stockInDTO.getDeletedDetails());
            savedStockIn.setCode(oldStock.getCode());
            savedStockIn.setCreatedDate(oldStock.getCreatedDate());
        } else {
            savedStockIn.setCreatedDate(LocalDateTime.now());
            savedStockIn.setCode(CodeHelper.spawnCode("import", LocalDateTime.now()));
        }
        savedStockIn.setEnable(true);
        savedStockIn.setBillInvoice(BigDecimal.valueOf(0));

        for (StockInDetail detail : details) {
            Product product = productRepository.findById(detail.getProduct().getId()).orElse(null);
            if (product == null)
                throw new IllegalStateException("Không tìm thấy thông tin sản phẩm");

            product.setQuantity(product.getQuantity() + detail.getQuantity());
            if (product.getQuantity() < 10)
                product.setStatus(ProductStatusEnum.LOW_STOCK.status);
            product.setStatus(ProductStatusEnum.IN_STOCK.status);
            productRepository.save(product);
            detail.setProduct(product);
            detail.setImportPrice(product.getImportPrice());
            detail.setTotal(detail.getImportPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
            savedStockIn.setBillInvoice(savedStockIn.getBillInvoice().add(detail.getTotal()));
            detail.setStockIn(savedStockIn);
        }

        savedStockIn.setStockInDetails(details);
        System.out.println(savedStockIn.getStockInDetails());
        return stockInRepository.save(savedStockIn).getId() != null;
    }

    @Override
    public Boolean delete(String id) {
        log.info("service: delete stock");
        try {
            StockIn stockIn = stockInRepository.findById(Long.valueOf(id)).orElse(null);
            if (stockIn == null)
                return false;
            stockIn.setEnable(false);
            stockInRepository.save(stockIn);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("ID không hợp lệ");
        }
        return true;
    }
}
