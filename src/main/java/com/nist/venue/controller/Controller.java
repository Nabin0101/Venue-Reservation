package com.nist.venue.controller;

import com.nist.venue.entity.Devicetoken;
import com.nist.venue.entity.User;
import com.nist.venue.repository.DeviceTokenRepository;
import com.nist.venue.repository.UserRepository;
import com.nist.venue.service.DeviceTokenService;
import com.nist.venue.service.JwtService;
import com.nist.venue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Validated
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
public class Controller {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private DeviceTokenService deviceTokenService;


    @Autowired
    private  DeviceTokenRepository deviceTokenRepository;



    @Autowired
    private AuthenticationManager authenticationManager;



    @Autowired
    UserService userService;




//user register api
    @PostMapping("/addNewUser")
    public ResponseEntity<Integer> addNewUser(@RequestBody User user) {
        Optional<User> optional = userRepository.findByUsername(user.getUsername());

        if (optional.isPresent()) {
            System.out.println("Username already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            try {
                userService.saveUser(user);
                return ResponseEntity.ok(user.getId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
    }



//Api for generating the token and user authentication
    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody User authRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            System.out.println("found");
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            System.out.println("not found");
            throw new UsernameNotFoundException("invalid user request !");
        }


    }









//to add the fcm token of the user int the database
    @PostMapping("user/addUserToken/{id}")
    public String addUserToken(@PathVariable int id,@RequestBody Devicetoken devicetoken){
        try{
            System.out.println(devicetoken.getToken());
            userService.addTokenToUser(id, devicetoken);
            return "Succesfull";

        }catch (Exception e){
            return "failed"+e;

        }



    }


    //this is to

    @GetMapping("user/findDeviceToken/{username}")
    public ResponseEntity<?> expireToken(@PathVariable String username) {
        Optional<List<DeviceTokenRepository.DeviceTokenProjection>> optionalDeviceToken = deviceTokenRepository.findByUsername(username);

        List<DeviceTokenRepository.DeviceTokenProjection> projections = optionalDeviceToken.orElse(Collections.emptyList());

        if (!projections.isEmpty()) {
            List<String> tokens = new ArrayList<>();

            for (DeviceTokenRepository.DeviceTokenProjection projection : projections) {
                // Assuming 'getToken()' is the method to retrieve the token value
                String token = projection.getToken(); // Adapt this to your actual method to retrieve the token
                tokens.add(token);
            }

            return ResponseEntity.ok().body(tokens);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projection not found");
        }
    }



    //when user logsout the fcm token is
    @DeleteMapping("user/deletefcmtoken/{token}")
    public ResponseEntity<String> deleteToken(@PathVariable String token){
        Optional<Devicetoken> findDeviceToken=deviceTokenRepository.findByToken(token);
        if(findDeviceToken.isPresent()){
            deviceTokenRepository.deleteByTokenId(findDeviceToken.get().getId());
            return  new ResponseEntity("Successfull",HttpStatus.OK);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not remove");

        }

    }
















    //this api updates the user profile from inside the app also requires the api token bearer
    @PutMapping("user/userUpdate")
    @PreAuthorize("hasAuthority('User')")
    public ResponseEntity<String> updateUser(@RequestBody User user){
        try{
            userService.updateUser(user);
            return ResponseEntity.ok("Successfull");

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }

    }



//username verification and phone number fetching for otp password change
    @PostMapping("user/passwordRecover/{username}")
    public ResponseEntity<String> passwordRecover(@PathVariable String username){
        Optional<User> optional = userRepository.findByUsername(username);
        if (optional.isPresent()) {
            User user = optional.get();
            return ResponseEntity.ok(user.getPhonenumber());

        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }


    }

    //passsword recovering from otp verification

    @PutMapping("user/updatePasswordOtp")
    public ResponseEntity<String> passwordUpdateOtp(@RequestBody User user){
        Optional<User> optional=userRepository.findByUsername(user.getUsername());
        if(optional.isPresent()){
            User userUpdate=optional.get();
            userUpdate.setPassword(user.getPassword());
            userUpdate.setConfirmpassword(user.getConfirmpassword());
            userService.saveUser(userUpdate);
            return ResponseEntity.ok("Succesfully Updtated");

        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }
    }


//get userprofile for in app editing
    @GetMapping("user/getUserInfo/{username}")
    public ResponseEntity<User> getUserinfo(@PathVariable String username){

        User userRequired=new User();
        Optional<User> optional=userRepository.findByUsername(username);
        if(optional.isPresent()){
            User user=optional.get();

            userRequired.setId(user.getId());

            userRequired.setName(user.getName());
            userRequired.setPhonenumber(user.getPhonenumber());
            userRequired.setEmail(user.getEmail());
            userRequired.setProfile(user.getProfile());

            return  ResponseEntity.ok(userRequired);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

//Inapp passwordChnage with username and password
    @PostMapping("user/userPasswordChange")
    public ResponseEntity<String> verifyUser(@RequestBody User authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                User updatedPassword;
                updatedPassword = new User();
                Optional<User> optional=userRepository.findByUsername(authRequest.getUsername());
                updatedPassword=optional.get();
                updatedPassword.setPassword(authRequest.getNewpassword());
                updatedPassword.setConfirmpassword(authRequest.getConfirmnewpassword());
                userService.saveUser(updatedPassword);

                return ResponseEntity.ok("Password Changed Successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Current Password");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authentication failed: " + e.getMessage());
        }
    }















}



