package com.tourbooking.tour_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TourBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourBookingApplication.class, args);
	}

}
