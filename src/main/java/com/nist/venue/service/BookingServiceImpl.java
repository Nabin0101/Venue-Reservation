package com.nist.venue.service;

import com.nist.venue.entity.Booking;
import com.nist.venue.entity.UserFavourite;
import com.nist.venue.entity.Venue;
import com.nist.venue.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements  BookingService {


    @Autowired
    BookingRepository bookingRepository;

    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<Venue> getBookingByUser(int userId) {
        List<Booking> userBooking = bookingRepository.findByUserId(userId);
        List<Venue> venues = extractVenuesFromBooking(userBooking);
        return venues;

    }


    private List<Venue> extractVenuesFromBooking(List<Booking> userBooking) {

        List<Venue> venues = new ArrayList<>();


        for (Booking userBookings : userBooking) {
            Venue venue = new Venue(userBookings.getVenue());
            venue.setBookedDate(userBookings.getDate());
            venue.setBookingId(userBookings.getId());
            if (venue != null) {

                venues.add(venue);
            }

        }
        return venues;


    }

    @Override
    public List<Venue> getBookingDates(int venueId) {
        List<Booking> bookings = bookingRepository.findByVenueId(venueId);
        List<Venue> venues = extractVenuesFromBookingByVenueId(bookings);
        return venues;

    }

    @Override
    public long getTotalBookings() {
        return bookingRepository.count();
    }

    @Override
    public List<Map<String, Object>> getBookingDetails() {
        List<Booking> bookings = bookingRepository.findAll();
        List<Map<String, Object>> bookingDetailsList = new ArrayList<>();

        for (Booking booking : bookings) {
            Map<String, Object> bookingDetailsMap = new HashMap<>();
            bookingDetailsMap.put("userPhoto", booking.getUser().getProfile());
            bookingDetailsMap.put("username", booking.getUser().getUsername());
            Date bookingDate = booking.getDate();
            // Assuming booking.getDate() returns a java.util.Date object
            String formattedDate = bookingDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
            bookingDetailsMap.put("bookingDate", formattedDate);
            bookingDetailsMap.put("venueName", booking.getVenue().getName());
            bookingDetailsList.add(bookingDetailsMap);
        }

        return bookingDetailsList;
    }

    @Override
    public boolean deleteBookingById(int bookingId) {
        try{
            bookingRepository.deleteById(bookingId);
            return true;
        }catch (Exception e){
            return false;

        }
    }


    private List<Venue> extractVenuesFromBookingByVenueId(List<Booking> userBooking) {
        List<Venue> venues = new ArrayList<>();
        for (Booking userBookings : userBooking) {
            Venue venue = new Venue(userBookings.getVenue()); // Creating a new instance of Venue
            if (venue != null) {
                venue.setBookedDate(userBookings.getDate());
                System.out.println(userBookings.getDate());
                venues.add(venue);
                System.out.println(venue.getBookedDate());
            }
        }
        return venues;
    }
}



