package com.tourbooking.tour_booking.mapper;

import com.tourbooking.tour_booking.dto.promotion.PromotionCreate;
import com.tourbooking.tour_booking.dto.promotion.PromotionUpdate;
import com.tourbooking.tour_booking.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    @Mapping(target = "expireddate", expression = "java(convertStringToLocalDate(promotionCreate.getExpireddate()))")
    Promotion toPromotion(PromotionCreate promotionCreate);

    default LocalDate convertStringToLocalDate(String expireddate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(expireddate, formatter);
    }

    void updatePromotionFromDto(PromotionUpdate promotionUpdate, @MappingTarget Promotion promotion);
}
