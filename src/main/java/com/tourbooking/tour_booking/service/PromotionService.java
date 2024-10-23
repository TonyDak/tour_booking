package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.dto.promotion.PromotionCreate;
import com.tourbooking.tour_booking.dto.promotion.PromotionUpdate;
import com.tourbooking.tour_booking.entity.Promotion;
import com.tourbooking.tour_booking.mapper.PromotionMapper;
import com.tourbooking.tour_booking.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionService {


    private final PromotionRepository promotionRepository;


    private final PromotionMapper promotionMapper;

    public PromotionCreate createPromotion(PromotionCreate promotionCreate) {

        if (promotionRepository.existsByCode(promotionCreate.getCode())) {
            throw new RuntimeException("Promotion code already exists");
        }
        Promotion promotion = promotionMapper.toPromotion(promotionCreate);
        promotionRepository.save(promotion);
        return promotionCreate;
    }
    public PromotionUpdate updatePromotion(String id, PromotionUpdate promotionUpdate) {
        Optional<Promotion> existingPromotionOpt = promotionRepository.findById(id);

        if (existingPromotionOpt.isEmpty()) {
            throw new RuntimeException("Promotion not found");
        }

        Promotion existingPromotion = existingPromotionOpt.get();


        promotionMapper.updatePromotionFromDto(promotionUpdate, existingPromotion);


        if (promotionUpdate.getExpireddate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate expireddate = LocalDate.parse(promotionUpdate.getExpireddate(), formatter);
            existingPromotion.setExpireddate(expireddate);
        }

        promotionRepository.save(existingPromotion);
        return promotionUpdate;
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

}
