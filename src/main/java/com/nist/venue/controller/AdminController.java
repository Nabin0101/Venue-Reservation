package com.nist.venue.controller;


import com.nist.venue.entity.Admin;
import com.nist.venue.entity.ChangePasswordRequest;
import com.nist.venue.entity.User;
import com.nist.venue.entity.Venue;
import com.nist.venue.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
public class AdminController {
    @Autowired
    AdminService adminService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    VenueService venueService;
    @Autowired
    UserService userService;

    @Autowired
    BookingService bookingService;

    @CrossOrigin
    @PostMapping("/admin/addNewAdmin")
    public ResponseEntity<String> addNewAdmin(@RequestBody Admin admin) {
        try {
            adminService.saveAdmin(admin);
            return ResponseEntity.ok("Successfully saved");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin
    @GetMapping("/getAdminDetails/{username}")
    public ResponseEntity<Admin> getAdminDetails(@PathVariable("username") String username) {
        // Use the username to fetch admin details from the service layer
        Admin admin = adminService.findUserByUsername(username);

        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @PutMapping("/updateAdminInfo/{username}")
    public ResponseEntity<String> updateAdminInfo(@Valid @RequestBody Admin admin, @PathVariable("username") String username) {
        try {
            // Update the admin information using the provided username
            adminService.updateAdmin(admin, username);
            return ResponseEntity.ok("Successfully Updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @CrossOrigin
    @DeleteMapping("/deleteAdmin/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable("id") int id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.ok("Deleted Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin
    @PostMapping("/changePassword/{username}")
    public ResponseEntity<?> changePassword(@PathVariable String username, @RequestBody ChangePasswordRequest request) {
        try {
            adminService.changePassword(username, request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while changing password");
        }
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Admin admin = adminService.getUserNameAndPassword(username, password);

        if (admin != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("adminName", admin.getUsername());
            response.put("name",admin.getFullName());
            response.put("message", "Successfully logged in!");
            return ResponseEntity.ok(response);
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }


    @CrossOrigin
    @GetMapping("/admin/userInfo")
    public ResponseEntity<List<Map<String, Object>>> getUserInfo() {
        try {
            List<Map<String, Object>> userInfoList = adminService.getUserInfo();
            return ResponseEntity.ok(userInfoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @CrossOrigin
    @GetMapping("admin/venueListInfo")
    public ResponseEntity<List<Venue>> getVenueInfo() {
        try {
            List<Venue> venueList = adminService.getVenueList();
            return ResponseEntity.ok(venueList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin
    @GetMapping("/venue/count")
    public ResponseEntity<Object> getVenueCount() {
        try {
            long totalVenues = venueService.getTotalVenues();
            System.out.println(totalVenues);
            Map<String, Long> response = new HashMap<>();
            response.put("count", totalVenues);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve venue count: " + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/user/count")
    public ResponseEntity<Object> getUserCount() {
        try {
            long totalUsers = userService.getTotalUsers();
            Map<String, Long> response = new HashMap<>();
            response.put("count", totalUsers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve user count: " + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/booking/count")
    public ResponseEntity<Object> getBookingCount() {
        try {
            long totalBooking = bookingService.getTotalBookings();
            Map<String, Long> respose = new HashMap<>();
            respose.put("count", totalBooking);
            return ResponseEntity.ok(respose);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve booking count:" + e.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/admin/deleteVenueRequest/{id}")
    public ResponseEntity<String> deleteVenue(@PathVariable int id) {
        try {
            adminService.deleteVenue(id);
            return ResponseEntity.ok("Successfully Deleted");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save the venue: " + e.getMessage());
        }
    }
}

//    @CrossOrigin
//    @GetMapping("/getUserName")
//    public ResponseEntity<Object> getLoggedInUserName() {
//        String username = adminService.getLoggedInUserName();
//        System.out.println(username);
//        if (username != null && !username.isEmpty()) {
//            Map<String, String> response = new HashMap<>();
//            response.put("username", username);
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
