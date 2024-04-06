package com.nist.venue.service;

import com.nist.venue.entity.UserFavourite;
import com.nist.venue.entity.Venue;

import java.util.List;

public interface FavouriteService {

   public UserFavourite saveFavourite(UserFavourite userFavourite);
    public List<Venue> getUserFavouritesByUserId(int userId);
    public void delete(int userId,int venueId);
}
