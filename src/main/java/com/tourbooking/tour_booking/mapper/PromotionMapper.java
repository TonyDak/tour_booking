package com.tourbooking.tour_booking.mapper;

import com.tourbooking.tour_booking.dto.promotion.PromotionRequest;
import com.tourbooking.tour_booking.dto.promotion.PromotionInfoRequest;
import com.tourbooking.tour_booking.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    //toPromotionInfoRequest
    PromotionInfoRequest toPromotionInfoRequest(Promotion promotion);

    //toPromotion
    Promotion toPromotion(PromotionRequest promotionRequest);

    //updatePromotionFromDto
    void updatePromotionFromDto(PromotionRequest promotionRequest, @MappingTarget Promotion promotion);
}
