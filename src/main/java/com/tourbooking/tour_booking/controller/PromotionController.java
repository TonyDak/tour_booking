package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.promotion.PromotionCreate;

import com.tourbooking.tour_booking.dto.promotion.PromotionUpdate;
import com.tourbooking.tour_booking.entity.Promotion;
import com.tourbooking.tour_booking.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping
    public ApiResponse<PromotionCreate> createPromotion(@RequestBody @Valid PromotionCreate promotionCreate) {
        ApiResponse<PromotionCreate> response = new ApiResponse<>();
        response.setMessage("Promotion created");
        response.setResult(promotionService.createPromotion(promotionCreate));
        return response;
    }

    @PutMapping("/{id}")
    public ApiResponse<PromotionUpdate> updatePromotion(@PathVariable String id, @RequestBody @Valid PromotionUpdate promotionUpdate) {
        ApiResponse<PromotionUpdate> response = new ApiResponse<>();
        PromotionUpdate updatedPromotion = promotionService.updatePromotion(id, promotionUpdate);
        response.setMessage("Promotion updated successfully");
        response.setResult(updatedPromotion);
        return response;
    }

    @GetMapping
    public ResponseEntity<List<Promotion>> getAllPromotions() {
        List<Promotion> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(promotions);
    }



}
