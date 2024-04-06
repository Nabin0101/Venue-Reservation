package com.nist.venue.repository;

import com.nist.venue.entity.UserFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface FavouriteRepository extends JpaRepository<UserFavourite,Integer> {

    List<UserFavourite> findByUserId(int userId);

    @Transactional
    void deleteByUserIdAndVenueId(int userId, int venueId);

    void deleteByVenueId(int venueId);
    int countByVenueId(int venuId);

}

