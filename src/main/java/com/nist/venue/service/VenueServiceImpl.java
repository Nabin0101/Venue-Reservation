package com.nist.venue.service;

import com.nist.venue.entity.*;
import com.nist.venue.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VenueServiceImpl implements VenueService {
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    FavouriteRepository favouriteRepository;


    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    BookingRepository bookingRepository;




    @Override
    public Venue saveVenue(Venue venue) {
        try {
            Optional<User> userFind = userRepository.findByUsername(venue.getUsername());
            if (userFind.isPresent()) {
                return venueRepository.save(venue);

            }

        } catch (Exception e) {
            return null;


        }

        return null;


    }

    @Override
    public List<Venue> getVenue(int userId) {
        // Retrieve all venues
        List<Venue> venues = venueRepository.findByVerifedIsTrue();

        //get the userDetails=

        // Retrieve the user's favorites
        List<UserFavourite> userFavourites = favouriteRepository.findByUserId(userId);

        // Retrieve ratings given by the user
        List<Rating> userRatings = ratingRepository.findByUserId(userId);

        // Calculate average rating for each venue across all users
        Map<Integer, Double> venueAverageRatings = calculateVenueAverageRatings();

        Map<Integer, Long> venueRatingCounts = calculateVenueRatingCounts();

        for (Venue venue : venues) {



            String ownerUsername = venue.getUsername(); // Assuming you have a method to get the owner's username
            Optional<User> user1= userRepository.findByUsername(ownerUsername);

            venue.setOwnerUrl(user1.get().getProfile());
            // Check if the venue is favorited by the user
            boolean isFavoritedByUser = isVenueFavoritedByUser(venue, userFavourites);

            // Get the rating given by the user for the venue
            double userRating = ratingByUser(venue, userId, userRatings);


            venue.setRating(userRating);


            // Set if the venue is favorited by the user
            venue.setFavourited(isFavoritedByUser);
            int venueFavouritesCount = countVenueFavourites(venue.getId(), userFavourites);
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



    //geting the number of people that liked this
    private int countVenueFavourites(int venueId, List<UserFavourite> userFavourites) {
        int count = 0;
        for (UserFavourite favourite : userFavourites) {
            if (favourite.getVenue().getId() == venueId) {
                count++;
            }
        }
        return count;
    }

/// get the count for each venue like one star two star son up to 5 star for summary
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


    // Check if the venue is favorited by the user
    private boolean isVenueFavoritedByUser(Venue venue, List<UserFavourite> userFavorites) {
        return userFavorites.stream().anyMatch(favorite -> favorite.getVenue().getId() == venue.getId());
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
    public List<Venue> getVenuesByOwner(String userName) {
        try{
            return venueRepository.findByUsername(userName);
        }catch (Exception e){
            return  null;

        }

    }


    @Transactional
    @Override
    public void deleteVenue(int id) {
        ratingRepository.deleteByVenueId(id);
        favouriteRepository.deleteByVenueId(id);
        bookingRepository.deleteByVenueId(id);

        venueRepository.deleteById(id);

    }

    @Override
    public long getTotalVenues() {
        return venueRepository.count();
    }

    @Override
    public List<Venue> getVenueInfo() {
        return venueRepository.findAll();
    }

    @Override
    public void updateVenueVerified(Venue venue, int id) {
        Venue venue1 = venueRepository.findById(id);
        if(venue1!= null)
        {
            venue1.setVerifed(venue.isVerifed());
            venueRepository.save(venue1);
        }
    }

    @Override
    public List<Venue> checkAvailability(String dateString, int userId) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateString);

            List<Booking> bookings = bookingRepository.findByDate(date);
            List<Venue> allVenues = venueRepository.findAll();
            Map<Integer, Double> venueAverageRatings = calculateVenueAverageRatings();
            Map<Integer, Long> venueRatingCounts = calculateVenueRatingCounts();

            // Retrieve the user's favorites
            List<UserFavourite> userFavourites = favouriteRepository.findByUserId(userId);

            // Retrieve ratings given by the user
            List<Rating> userRatings = ratingRepository.findByUserId(userId);

            // Extract venues that are not booked for the specified date
            List<Venue> availableVenues = new ArrayList<>();
            for (Venue venue : allVenues) {
                boolean isBooked = false;
                for (Booking booking : bookings) {
                    if (booking.getVenue().equals(venue)) {
                        isBooked = true;
                        break;
                    }
                }
                if (!isBooked) {


                    // Calculate and set owner URL
                    String ownerUsername = venue.getUsername();
                    Optional<User> user1 = userRepository.findByUsername(ownerUsername);
                    venue.setOwnerUrl(user1.get().getProfile());

                    // Calculate and set rating
                    double userRating = ratingByUser(venue, userId, userRatings);
                    venue.setRating(userRating);

                    // Check if favorited by user and set
                    boolean isFavoritedByUser = isVenueFavoritedByUser(venue, userFavourites);
                    venue.setFavourited(isFavoritedByUser);

                    // Calculate and set total likes
                    int venueFavouritesCount = countVenueFavourites(venue.getId(), userFavourites);
                    venue.setTotalLikes(venueFavouritesCount);

                    // Set total ratings
                    venue.setTotalRatings(venueRatingCounts.getOrDefault(venue.getId(), 0L).intValue());

                    // Set average rating
                    venue.setAverageRating(venueAverageRatings.getOrDefault(venue.getId(), 0.0));

                    // Set star rating counts
                    Map<Integer, Long> starRatingCounts = calculateVenueStarRatingCounts().getOrDefault(venue.getId(), new HashMap<>());
                    venue.setOneStarCount(starRatingCounts.getOrDefault(1, 0L).intValue());
                    venue.setTwoStarCount(starRatingCounts.getOrDefault(2, 0L).intValue());
                    venue.setThreeStarCount(starRatingCounts.getOrDefault(3, 0L).intValue());
                    venue.setFourStarCount(starRatingCounts.getOrDefault(4, 0L).intValue());
                    venue.setFiveStarCount(starRatingCounts.getOrDefault(5, 0L).intValue());
                    availableVenues.add(venue);
                }
            }

            return availableVenues;
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception properly in your application
            return null;
        }


    }

    @Override
    public Venue updateVenue(Venue venue, int id) {
        Venue oldVenue=venueRepository.findById(id);
        if(oldVenue!=null){
            oldVenue.setName(venue.getName());
            oldVenue.setPhone(venue.getPhone());
            oldVenue.setLocation(venue.getLocation());
            oldVenue.setEmail(venue.getEmail());
            oldVenue.setPrice(venue.getPrice());
            oldVenue.setAbout(venue.getAbout());
            venueRepository.save(oldVenue);
        }


        return null;

    }


}










