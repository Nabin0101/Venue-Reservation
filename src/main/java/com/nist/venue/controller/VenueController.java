package com.nist.venue.controller;

import com.nist.venue.entity.Booking;
import com.nist.venue.entity.Venue;
import com.nist.venue.repository.DeviceTokenRepository;
import com.nist.venue.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class VenueController {

    @Autowired
    VenueService venueService;

    @Autowired
    DeviceTokenRepository deviceTokenRepository;

    // Add venue to the database
    @PostMapping("venue/addVenue")
    public ResponseEntity<String> addVenue(@RequestBody Venue venue) {


        try {
            System.out.println(venue.getUsername());
            venueService.saveVenue(venue);
            return ResponseEntity.ok("Successfully saved");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save the venue: " + e.getMessage());
        }


    }

    //extracts all the venues in the database
    @GetMapping("venue/getVenues/{userId}")
    public ResponseEntity<List<Venue>> getVenues(@PathVariable int userId) {
        try {
            System.out.println(userId);
            List<Venue> venues = venueService.getVenue(userId);
            return new ResponseEntity<>(venues, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);

        }
    }



    @GetMapping("venue/getVenuesByOwner/{userName}")
    public ResponseEntity<List<Venue>> getOwnerVenus(@PathVariable String userName) {
        try {

            List<Venue> venues = venueService.getVenuesByOwner(userName);
            return new ResponseEntity<>(venues, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);

        }
    }

    ///delete venue by id
    @DeleteMapping("owner/deleteVenues/{id}")
    public ResponseEntity<String> deleteVenue(@PathVariable int id) {
        try {
            venueService.deleteVenue(id);
            return ResponseEntity.ok("Successfully Deleted");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save the venue: " + e.getMessage());
        }


    }

    @PutMapping("owner/updateVenue/{id}")
    public ResponseEntity<String> UpdateVenue(@PathVariable int id ,@RequestBody Venue venue) {
        try {
            venueService.updateVenue(venue,id);
            return ResponseEntity.ok("Successfully Update");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to Update the venue: " + e.getMessage());
        }


    }

    @CrossOrigin
    @PutMapping("/venue/UpdateVerifiedVenue/{id}")
    public ResponseEntity<String> UpdateVerifiedVenue(@PathVariable int id ,@RequestBody Venue venue) {
        try {
            venueService.updateVenueVerified(venue,id);
            return ResponseEntity.ok("Successfully Update");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to Update the venue: " + e.getMessage());
        }
    }



    @GetMapping("/venue/checkAvailability")
    public ResponseEntity<List<Venue>> getBookingsForVenue(@RequestParam("date") String dateString, @RequestParam("userId") int userId) {
        try {
            List<Venue> bookingsForDate = venueService.checkAvailability(dateString, userId);
            return ResponseEntity.ok(bookingsForDate);
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("venue/getVenues")
    public ResponseEntity<List<Venue>> getVenues() {
        try {
            List<Venue> venues = venueService.getVenueInfo();
            return new ResponseEntity<>(venues, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);

        }
    }

}




