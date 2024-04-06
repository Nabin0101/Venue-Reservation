package com.nist.venue.controller;

import com.nist.venue.entity.Rating;
import com.nist.venue.entity.UserFavourite;
import com.nist.venue.repository.RatingRepository;
import com.nist.venue.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RatingController {
    @Autowired
    RatingRepository repository;

    @Autowired
    RatingService ratingService;




    //Rating a venue is done through save and update
    @PostMapping("venues/Rating")
    public ResponseEntity<String> saveRating(@RequestBody Rating rating) {
        try {
            Rating existing = repository.findByUserIdAndVenueId(rating.getUser().getId(), rating.getVenue().getId());

            if (existing == null) {
                ratingService.saveRating(rating);
                return ResponseEntity.ok("Rating saved successfully");
            } else {
                existing.setRating(rating.getRating());
                ratingService.saveRating(existing);
                return ResponseEntity.ok("Rating updated successfully");
            }
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving/updating rating");
        }
    }

    @CrossOrigin
    @GetMapping("/admin/getRatingDetails")
    public ResponseEntity<List<Map<String, Object>>> getRatingDetails() {
        try {
            List<Map<String, Object>> ratingDetailsList = ratingService.getRatingDetails();
            return ResponseEntity.ok(ratingDetailsList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
