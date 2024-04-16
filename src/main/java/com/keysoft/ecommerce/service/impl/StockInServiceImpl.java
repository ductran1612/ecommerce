package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.constant.ProductStatusEnum;
import com.keysoft.ecommerce.dto.ProductDTO;
import com.keysoft.ecommerce.dto.StockInDTO;
import com.keysoft.ecommerce.dto.StockInDetailDTO;
import com.keysoft.ecommerce.model.Product;
import com.keysoft.ecommerce.model.StockIn;
import com.keysoft.ecommerce.model.StockInDetail;
import com.keysoft.ecommerce.repository.ProductRepository;
import com.keysoft.ecommerce.repository.StockInRepository;
import com.keysoft.ecommerce.service.StockInService;
import com.keysoft.ecommerce.util.CodeHelper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class StockInServiceImpl implements StockInService {
    @Autowired
    private StockInRepository stockInRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Page<StockInDTO> getAllStockIns(StockInDTO stockInDTO) {
        log.info("service: get all stock in");
        Page<StockIn> page = stockInRepository.findAll(PageRequest.of(stockInDTO.getPage(), stockInDTO.getSize()));
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
            throw new NumberFormatException();
        }
        return stockInDTO;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public Boolean save(StockInDTO stockInDTO) throws IllegalAccessException {
        log.info("sevice: save stock in");
        if(stockInDTO.getId() != null)
            throw new IllegalStateException("Thông tin phiếu không phù hợp");
        StockIn savedStockIn = modelMapper.map(stockInDTO, StockIn.class);
        Set<StockInDetail> details = savedStockIn.getStockInDetails();

        for(StockInDetail detail : details) {
            Product product = productRepository.findById(detail.getProduct().getId()).orElse(null);
            if (product.getId() == null)
                throw new IllegalAccessException("Không tìm thấy thông tin sản phẩm");

            product.setQuantity(product.getQuantity() + detail.getQuantity());
            if(product.getQuantity() < 10)
                product.setStatus(ProductStatusEnum.LOW_STOCK.status);
            product.setStatus(ProductStatusEnum.IN_STOCK.status);
            productRepository.save(product);
            detail.setProduct(product);
            detail.setImportPrice(product.getImportPrice());
            detail.setStockIn(savedStockIn);
        }

        savedStockIn.setCreatedDate(LocalDateTime.now());
        savedStockIn.setCode(CodeHelper.spawnCode("import", LocalDateTime.now()));
        savedStockIn.setStockInDetails(details);
        return stockInRepository.save(savedStockIn).getId()!= null;
    }
}
