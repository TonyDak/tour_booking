package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.dto.promotion.ActivePromotionRequest;
import com.tourbooking.tour_booking.dto.promotion.PromotionRequest;
import com.tourbooking.tour_booking.dto.promotion.PromotionInfoRequest;
import com.tourbooking.tour_booking.entity.Promotion;
import com.tourbooking.tour_booking.mapper.PromotionMapper;
import com.tourbooking.tour_booking.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    //get all promotions
    public List<PromotionInfoRequest> getPromotions() {
        return promotionRepository.findAll().stream()
                .map(promotionMapper::toPromotionInfoRequest)
                .collect(Collectors.toList());
    }

    //create promotion
    public PromotionRequest createPromotion(PromotionRequest promotionRequest) {
        Promotion promotion = promotionMapper.toPromotion(promotionRequest);
        promotionRepository.save(promotion);
        return promotionRequest;
    }

    //update promotion
    public PromotionRequest updatePromotion(String id, PromotionRequest promotionRequest) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow(() -> new RuntimeException("Promotion not found"));
        promotionMapper.updatePromotionFromDto(promotionRequest, promotion);
        promotionRepository.save(promotion);
        return promotionRequest;
    }

    //active promotion
    public void activePromotion(ActivePromotionRequest activePromotionRequest) {
        Promotion promotion = promotionRepository.findById(activePromotionRequest.getPromotionId()).orElseThrow(() -> new RuntimeException("Promotion not found"));
        promotion.setActive(activePromotionRequest.getActive());
        promotionRepository.save(promotion);
    }
}
