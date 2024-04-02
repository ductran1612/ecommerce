package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.ProductDTO;
import com.keysoft.ecommerce.model.Product;
import com.keysoft.ecommerce.repository.ProductRepository;
import com.keysoft.ecommerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<ProductDTO> getAllProducts(ProductDTO productDTO) {
        log.info("service: get all products");
        Page<Product> page = productRepository.findAll(PageRequest.of(productDTO.getPage(), productDTO.getSize()));
        List<ProductDTO> results = new ArrayList<>();

        for(Product item : page.getContent()){
            ProductDTO dto = modelMapper.map(item, ProductDTO.class);
            results.add(dto);
        }
        return new PageImpl<>(results, PageRequest.of(productDTO.getPage(), productDTO.getSize()), page.getTotalElements());
    }

    @Override
    public boolean save(ProductDTO productDTO) {
        return false;
    }
}
