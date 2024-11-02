package com.tourbooking.tour_booking.mapper;


import com.tourbooking.tour_booking.dto.placevisit.PlaceVisitCreate;
import com.tourbooking.tour_booking.entity.PlaceVisits;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PlaceVisitMapper {
    PlaceVisitMapper INSTANCE = Mappers.getMapper(PlaceVisitMapper.class);

    @Mapping(target = "place", ignore = true) // We will set the place separately
    PlaceVisits toPlaceVisits(PlaceVisitCreate placeVisitCreate);

    // Optionally, create a method to convert PlaceVisits back to PlaceVisitCreate if needed
    PlaceVisitCreate toPlaceVisitCreate(PlaceVisits placeVisits);
}
