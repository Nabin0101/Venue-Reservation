package com.nist.venue.controller;

import com.nist.venue.entity.Booking;
import com.nist.venue.entity.Rating;
import com.nist.venue.entity.UserFavourite;
import com.nist.venue.entity.Venue;
import com.nist.venue.repository.BookingRepository;
import com.nist.venue.repository.RatingRepository;
import com.nist.venue.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.*;

@RestController
public class BookingController {

    @Autowired
    BookingService bookingService;


    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    RatingRepository ratingRepository;




    @PostMapping("/user/addBooking")
    public ResponseEntity<String> saveFavourites(@RequestBody Booking booking) {



        try {
            bookingService.saveBooking(booking);

            return ResponseEntity.ok("Successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }

    }


    @GetMapping("/user/getBookings/{userId}")
    public ResponseEntity<List<Venue>> getBookingsForUser(@PathVariable int userId) {

        try {
            List<Venue> venuesList = bookingService.getBookingByUser(userId);
            return new ResponseEntity<>(venuesList, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }
    }

    @GetMapping("/venue/getBookings/{venueId}")
    public ResponseEntity<List<Venue>>getBookingsForVenue(@PathVariable int venueId) {
        try {
            List<Venue> bookedDate = bookingService.getBookingDates(venueId);
            return new ResponseEntity<>(bookedDate, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }
    }

    @DeleteMapping("/venue/deleteBooking/{bookingId}")
    public ResponseEntity<String> deleteBookingHistory(@PathVariable int bookingId) {
        try {
            bookingService.deleteBookingById(bookingId);

            return ResponseEntity.ok("Successfully deleted thee") ;
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user favorite");
        }
    }

    @CrossOrigin
    @GetMapping("/admin/getBookingDetails")
    public ResponseEntity<List<Map<String, Object>>> getBookingDetails() {
        try {
            List<Map<String, Object>> bookingDetailsList = bookingService.getBookingDetails();
            return ResponseEntity.ok(bookingDetailsList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
