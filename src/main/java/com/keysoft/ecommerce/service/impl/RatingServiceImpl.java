package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.ProductDTO;
import com.keysoft.ecommerce.dto.RatingDTO;
import com.keysoft.ecommerce.repository.RatingRepository;
import com.keysoft.ecommerce.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public boolean crateRating(RatingDTO ratingDTO) {
        return false;
    }

    @Override
    public List<RatingDTO> getRatingByProduct(ProductDTO productDTO) {
        return null;
    }
}
