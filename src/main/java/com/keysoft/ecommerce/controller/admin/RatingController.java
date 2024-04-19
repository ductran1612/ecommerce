package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.dto.RatingDTO;
import com.keysoft.ecommerce.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/rating")
@Slf4j
public class RatingController {
    @Autowired
    private RatingService ratingService;
    @PostMapping("/create")
    public ResponseEntity<?> createRating(@RequestBody RatingDTO ratingDTO) {
        log.info("controller: create rating");
        try{
            if(ratingService.createRating(ratingDTO))
                return ResponseEntity.ok("Thành công");
            return ResponseEntity.badRequest().body("Thất bại");
        }catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listByProduct/{id}")
    public ResponseEntity<?> listByProduct(@PathVariable String id) {
        log.info("controller: list by product");
        try {
            return ResponseEntity.ok(ratingService.getRatingByProduct(id));
        }catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
