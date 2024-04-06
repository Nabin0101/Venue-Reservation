package com.nist.venue.repository;

import com.nist.venue.entity.Rating;
import com.nist.venue.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface VenueRepository extends JpaRepository<Venue,Integer> {
    List<Venue> findByUsername(String userName);
    Venue findById(int id);
    List<Venue> findByVerifedIsTrue();


}
