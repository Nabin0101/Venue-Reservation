package com.nist.venue.service;

import com.nist.venue.entity.Venue;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


public interface VenueService {

    public Venue saveVenue(Venue venue);
    public List<Venue> getVenue(int userId);
    public List<Venue> getVenuesByOwner(String userName);
    public void deleteVenue(int id);
    public long getTotalVenues();
    public  List<Venue> getVenueInfo();
    public void updateVenueVerified(Venue venue ,int id);
    public List<Venue> checkAvailability(String date,int userId);
    public Venue updateVenue(Venue venue, int id);



}
