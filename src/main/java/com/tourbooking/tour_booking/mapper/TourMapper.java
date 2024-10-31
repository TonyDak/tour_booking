package com.tourbooking.tour_booking.mapper;

import com.tourbooking.tour_booking.dto.gallery.GalleryCreate;
import com.tourbooking.tour_booking.dto.itinerary.ItineraryCreate;
import com.tourbooking.tour_booking.dto.location.LocationCreate;
import com.tourbooking.tour_booking.dto.placevisit.PlaceVisitCreate;
import com.tourbooking.tour_booking.dto.schedule.ScheduleCreate;
import com.tourbooking.tour_booking.dto.tour.TourCreate;
import com.tourbooking.tour_booking.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")
public interface TourMapper {
    @Mapping(target = "galleries", source = "galleries")
    @Mapping(target = "schedules", source = "schedules")
    @Mapping(target = "itineraries", source = "itineraries")
    @Mapping(target = "location", source = "location")

    Tour toTour(TourCreate tourCreate);

    @Mapping(target = "tour", ignore = true)
    Gallery toGallery(GalleryCreate galleryCreate);

    @Mapping(target = "tour", ignore = true)
    Schedule toSchedule(ScheduleCreate scheduleCreate);

    @Mapping(target = "tour", ignore = true)
    Itinerary toItinerary(ItineraryCreate itineraryCreate);

    @Mapping(target = "tour", ignore = true)
    Location toLocation(LocationCreate locationCreate);


    List<Gallery> toGalleries(List<GalleryCreate> galleryCreates);

    List<Schedule> toSchedules(List<ScheduleCreate> scheduleCreates);

    List<Itinerary> toItineraries(List<ItineraryCreate> itineraryCreates);
    TourCreate toTourCreate(Tour tour);
}
