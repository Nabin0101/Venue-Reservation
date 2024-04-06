package com.nist.venue.repository;

import com.nist.venue.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Integer> {
   List<Booking> findByUserId(int userId);
   void deleteByVenueId(int id);
   void deleteById(int id);
   List<Booking> findByVenueId(int venueId);

   @Query("SELECT b FROM Booking b WHERE FUNCTION('DATE', b.date) = :date")
   List<Booking> findByDate(@Param("date") Date date);

}
