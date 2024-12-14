package com.tourbooking.tour_booking.dto.gallery;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import com.tourbooking.tour_booking.entity.Gallery;

/**
 * DTO for {@link Gallery}
 */
@Value
@Valid

public class GalleryCreate {
    @NotBlank
    String path;

    @JsonCreator
    public GalleryCreate(@JsonProperty("path") String path) {
        this.path = path;
    }


}
