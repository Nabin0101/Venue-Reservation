package com.nist.venue.service;


import com.nist.venue.entity.Booking;
import com.nist.venue.entity.Rating;
import com.nist.venue.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;

@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    RatingRepository ratingRepository;


    @Override
    public Rating saveRating(Rating rating) {
        return ratingRepository.save(rating);


    }

    @Override
    public List<Map<String, Object>> getRatingDetails() {
        List<Rating> ratings =ratingRepository.findAll();
        List<Map<String, Object>> ratingDetailsList = new ArrayList<>();

        for(Rating rating : ratings) {
            Map<String, Object> ratingsDetailsMap = new HashMap<>();
           // ratingsDetailsMap.put("userPhoto", rating.getUser().getProfile());
            ratingsDetailsMap.put("name",rating.getUser().getName());
            ratingsDetailsMap.put("venueName",rating.getVenue().getName());
            ratingsDetailsMap.put("rating",rating.getRating());
            ratingDetailsList.add(ratingsDetailsMap);
        }

        return ratingDetailsList;
    }
}
