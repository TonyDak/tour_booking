package com.tourbooking.tour_booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Location {
    @Id
    private String id;
    private String name;
    private String avt;
    private String description;
    private Double latitude;
    private Double longitude;
    private Integer total_tour;

    @JsonIgnore
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Tour> tours;

    @PostLoad
    public void updateTotalTour() {
        this.total_tour = tours != null ? tours.size() : 0;

    }

}
