package com.nist.venue.repository;

import com.nist.venue.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Integer> {

    Rating findByUserIdAndVenueId(int userId, int venueId);
    void deleteByVenueId(int venueId);

    List<Rating> findByUserId(int userId);
}
