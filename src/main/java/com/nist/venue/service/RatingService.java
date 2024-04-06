package com.nist.venue.service;

import com.nist.venue.entity.Rating;

import java.util.List;
import java.util.Map;

public interface RatingService {

    public Rating saveRating(Rating rating);
    public List<Map<String, Object>> getRatingDetails();
}
