package com.nist.venue.controller;

import com.nist.venue.entity.User;
import com.nist.venue.entity.UserFavourite;
import com.nist.venue.entity.Venue;
import com.nist.venue.service.FavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FavoriteContoller {

    @Autowired
    FavouriteService favouriteService;




    @DeleteMapping("/user/deletefavorites")
    public ResponseEntity<String> deleteFavorite(@RequestBody UserFavourite userFavourite) {
        try {
            System.out.println("hello there i am here");

            int userId = userFavourite.getUser().getId();
            int venueId = userFavourite.getVenue().getId();



            favouriteService.delete(userId, venueId);

            return ResponseEntity.ok("Successfully deleted user favorite for userId: " + userId + " and venueId: " + venueId);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user favorite");
        }
    }



    @PostMapping("/user/addfavourites")
    public ResponseEntity<String> saveFavourites(@RequestBody UserFavourite userFavourite) {
        try {
            favouriteService.saveFavourite(userFavourite);
            System.out.println(userFavourite.getVenue());
            return ResponseEntity.ok("Successfull");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }

    }



    /// api to the favourites of the user

    @GetMapping("/user/favourites/{userId}")
    public ResponseEntity<List<Venue>> getVenues(@PathVariable int userId) {
        try {

            List<Venue> venues = favouriteService.getUserFavouritesByUserId(userId);


            return new ResponseEntity<>(venues, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);

        }
    }

}
