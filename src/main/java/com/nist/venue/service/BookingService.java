package com.nist.venue.service;

import com.nist.venue.entity.Booking;
import com.nist.venue.entity.Venue;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookingService {
    public Booking saveBooking(Booking booking);
    public List<Venue> getBookingByUser(int userId);
    public List<Venue> getBookingDates(int venueId);
    public long getTotalBookings();
    public  List<Map<String, Object>> getBookingDetails();
    public boolean deleteBookingById(int bookingId);

}
