package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;

import com.tourbooking.tour_booking.dto.promotion.ActivePromotionRequest;
import com.tourbooking.tour_booking.dto.promotion.PromotionRequest;
import com.tourbooking.tour_booking.dto.promotion.PromotionInfoRequest;
import com.tourbooking.tour_booking.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;


    //get all promotions
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<List<PromotionInfoRequest>> getPromotions(){
        ApiResponse<List<PromotionInfoRequest>> response = new ApiResponse<>();
        response.setMessage("Promotions");
        response.setResult(promotionService.getPromotions());
        return response;
    }

    //create promotion
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<PromotionRequest> createPromotion(@RequestBody PromotionRequest promotionRequest) {
        ApiResponse<PromotionRequest> response = new ApiResponse<>();
        response.setMessage("Promotion created");
        response.setResult(promotionService.createPromotion(promotionRequest));
        return response;
    }

    //update promotion
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<PromotionRequest> updatePromotion(@PathVariable String id,@RequestBody PromotionRequest promotionRequest) {
        ApiResponse<PromotionRequest> response = new ApiResponse<>();
        response.setMessage("Promotion updated");
        response.setResult(promotionService.updatePromotion(id, promotionRequest));
        return response;
    }

    //active promotion
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<Void> activePromotion(@RequestBody ActivePromotionRequest activePromotionRequest) {
        promotionService.activePromotion(activePromotionRequest);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Promotion updated");
        return response;

    }



}
