package com.nist.venue.service;

import com.nist.venue.entity.Devicetoken;
import com.nist.venue.entity.User;
import com.nist.venue.repository.DeviceTokenRepository;
import com.nist.venue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService , UserDetailsService {
    @Autowired
    UserRepository userRepository;


    @Autowired
    PasswordEncoder endcoder;
    @Override
    public User saveUser(User user) {

        user.setPassword(endcoder.encode(user.getPassword()));
        user.setConfirmpassword(endcoder.encode(user.getConfirmpassword()));
        return userRepository.save(user);

    }

    @Override
    public User findUserById(int id) {
        Optional<User> UserFound=userRepository.findById(id);

        return UserFound.orElse(null);
    }

    @Override
    public void updateUser(User user) {
        Optional<User> userDetails=userRepository.findByUsername(user.getUsername());
        if(userDetails.isPresent()){
            User userUpdate=userDetails.get();
            userUpdate.setProfile(user.getProfile());
            userUpdate.setName(user.getName());
            userUpdate.setPhonenumber(user.getPhonenumber());
            userUpdate.setEmail(user.getEmail());
//
            userRepository.save(userUpdate);
        }






    }

    @Override
    public void addTokenToUser(int id, Devicetoken devicetoken) {
        User user =findUserById(id);
        user.addToken(devicetoken);

        userRepository.save(user);

    }

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail=userRepository.findByUsername(username);
        return userDetail.map(UserInfoDetails::new).orElseThrow(()-> new UsernameNotFoundException("Username not found"));

    }
}
