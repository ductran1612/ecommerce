package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.CustomerDTO;
import com.keysoft.ecommerce.dto.RatingDTO;
import com.keysoft.ecommerce.model.Product;
import com.keysoft.ecommerce.model.Rating;
import com.keysoft.ecommerce.repository.CustomerRepository;
import com.keysoft.ecommerce.repository.ProductRepository;
import com.keysoft.ecommerce.repository.RatingRepository;
import com.keysoft.ecommerce.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public boolean createRating(RatingDTO ratingDTO) {
        log.info("service: create rating");
        Rating rating = modelMapper.map(ratingDTO, Rating.class);
        if(rating.getProduct().getId() == null)
            throw new IllegalStateException("Thông tin sản phẩm trong phản hồi không hợp lệ");
        if(rating.getCustomer().getUsername() == null)
            throw new IllegalStateException("Thông tin khách hàng không hợp lệ");
        rating.setCreatedDate(LocalDateTime.now());
        rating.setProduct(productRepository.findById(rating.getProduct().getId()).orElse(null));
        rating.setCustomer(customerRepository.findByUsername(rating.getCustomer().getUsername()).orElse(null));
        return ratingRepository.save(rating).getId() != null;
    }

    @Override
    public List<RatingDTO> getRatingByProduct(String productId) {
        log.info("service: find by product");
        try {
            Product product = productRepository.findById(Long.valueOf(productId)).orElse(null);
            List<Rating> ratingList = ratingRepository.findByProduct(product);
            List<RatingDTO> results = new ArrayList<>();
            for(Rating rating : ratingList) {
                results.add(modelMapper.map(rating, RatingDTO.class));
            }
            return results;
        }catch (NumberFormatException e) {
            throw new IllegalStateException("Thông tin sản phẩm không hợp lệ");
        }
    }

    @Override
    public Double getAverageRatingByProduct(String productId) {
        List<RatingDTO> ratingDTOS = getRatingByProduct(productId);
        if(ratingDTOS.isEmpty())
            return 0.0;
        int sum = 0;
        for(RatingDTO ratingDTO : ratingDTOS) {
            sum += ratingDTO.getRating();
        }

        return (double) sum / ratingDTOS.size();
    }
}
