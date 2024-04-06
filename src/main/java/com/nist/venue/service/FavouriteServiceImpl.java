package com.nist.venue.service;

import com.nist.venue.entity.Rating;
import com.nist.venue.entity.User;
import com.nist.venue.entity.UserFavourite;
import com.nist.venue.entity.Venue;
import com.nist.venue.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FavouriteServiceImpl implements FavouriteService{


    @Autowired
    FavouriteRepository favouriteRepository;
    VenueRepository venueRepository;
    @Autowired
    UserRepository userRepository;


    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    BookingRepository bookingRepository;



    @Override
    public UserFavourite saveFavourite(UserFavourite userFavourite) {
        return favouriteRepository.save(userFavourite);

    }

    @Override
    public List<Venue> getUserFavouritesByUserId(int userId) {
        List<UserFavourite> userFavourites = favouriteRepository.findByUserId(userId);
        List<Rating> userRatings = ratingRepository.findByUserId(userId);

        List<Venue> venues = extractVenuesFromUserFavorites(userFavourites);

        Map<Integer, Double> venueAverageRatings = calculateVenueAverageRatings();

        Map<Integer, Long> venueRatingCounts = calculateVenueRatingCounts();
        for (Venue venue : venues) {



            String ownerUsername = venue.getUsername(); // Assuming you have a method to get the owner's username
            Optional<User> user1= userRepository.findByUsername(ownerUsername);

            venue.setOwnerUrl(user1.get().getProfile());
            // Check if the venue is favorited by the user

            // Get the rating given by the user for the venue
            double userRating = ratingByUser(venue, userId, userRatings);


            venue.setRating(userRating);


            // Set if the venue is favorited by the user
            int venueFavouritesCount = favouriteRepository.countByVenueId(venue.getId());
            venue.setTotalLikes(venueFavouritesCount);// Assuming you have a method to count venue favor

            ///geting the total ratings
            venue.setTotalRatings(venueRatingCounts.getOrDefault(venue.getId(), 0L).intValue());

            // Set the average rating for the venue across all users
            venue.setAverageRating(venueAverageRatings.getOrDefault(venue.getId(), 0.0));

            Map<Integer, Long> starRatingCounts = calculateVenueStarRatingCounts().getOrDefault(venue.getId(), new HashMap<>());
            venue.setOneStarCount(starRatingCounts.getOrDefault(1, 0L).intValue());
            venue.setTwoStarCount(starRatingCounts.getOrDefault(2, 0L).intValue());
            venue.setThreeStarCount(starRatingCounts.getOrDefault(3, 0L).intValue());
            venue.setFourStarCount(starRatingCounts.getOrDefault(4, 0L).intValue());
            venue.setFiveStarCount(starRatingCounts.getOrDefault(5, 0L).intValue());


        }

        return venues;
    }
    private Map<Integer, Map<Integer, Long>> calculateVenueStarRatingCounts() {
        List<Rating> allRatings = ratingRepository.findAll();
        return allRatings.stream()
                .collect(Collectors.groupingBy(
                        rating -> rating.getVenue().getId(),
                        Collectors.groupingBy(
                                rating -> (int) rating.getRating(), // Assuming rating is a double, cast it to int for star rating
                                Collectors.counting()
                        )
                ));
    }





    private int countVenueFavourites(int venueId, List<UserFavourite> userFavourites) {
        int count = 0;
        for (UserFavourite favourite : userFavourites) {
            if (favourite.getVenue().getId() == venueId) {
                count++;
            }
        }
        return count;
    }

    //Extracting the venues from the users
    private List<Venue> extractVenuesFromUserFavorites(List<UserFavourite> userFavorites) {

        List<Venue> venues = new ArrayList<>();


        for (UserFavourite userFavourite : userFavorites) {
            Venue venue = userFavourite.getVenue();
            if (venue != null) {

                venues.add(venue);
            }
        }

        return venues;
    }

    // Get the rating given by the user for the venue
    private double ratingByUser(Venue venue, int userId, List<Rating> userRatings) {
        OptionalDouble userRating = userRatings.stream()
                .filter(rating -> rating.getVenue().getId() == venue.getId() && rating.getUser().getId() == userId)
                .mapToDouble(Rating::getRating)
                .findFirst();
        return userRating.orElse(0.0);
    }

    // Calculate average rating for each venue across all users
    private Map<Integer, Double> calculateVenueAverageRatings() {
        List<Rating> allRatings = ratingRepository.findAll();
        return allRatings.stream()
                .collect(Collectors.groupingBy(rating -> rating.getVenue().getId(),
                        Collectors.averagingDouble(Rating::getRating)));
    }


    private Map<Integer, Long> calculateVenueRatingCounts() {
        List<Rating> allRatings = ratingRepository.findAll();
        return allRatings.stream()
                .collect(Collectors.groupingBy(rating -> rating.getVenue().getId(), Collectors.counting()));
    }


    @Override
    public void delete(int userId, int venueId) {
        favouriteRepository.deleteByUserIdAndVenueId(userId,venueId);
        System.out.println("User id"+userId);
        System.out.println("venue_id "+userId);
    }




}
