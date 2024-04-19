package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.ProductDTO;
import com.keysoft.ecommerce.dto.RatingDTO;

import java.util.List;

public interface RatingService {
    boolean createRating(RatingDTO ratingDTO);
    List<RatingDTO> getRatingByProduct(String productId);
    Double getAverageRatingByProduct(String productId);
}
