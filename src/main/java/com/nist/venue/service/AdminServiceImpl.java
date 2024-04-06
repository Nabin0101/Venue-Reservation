package com.nist.venue.service;


import com.nist.venue.entity.Admin;
import com.nist.venue.entity.User;
import com.nist.venue.entity.Venue;
import com.nist.venue.repository.AdminRepository;
import com.nist.venue.repository.UserRepository;
import com.nist.venue.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VenueRepository venueRepository;

    @Override
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public void updateAdmin(Admin admin, String username) {
        Admin admin1 = adminRepository.findByUsername(username);
        if (admin1 != null) {
            admin1.setFullName(admin.getFullName());
            admin1.setEmail(admin.getEmail());
            admin1.setPhoneNumber(admin.getPhoneNumber());
            admin1.setUsername(admin.getUsername());
            adminRepository.save(admin1);
        }

    }


    @Override
    public void deleteAdmin(int id) {
        adminRepository.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> getUserInfo() {
        List<User> users = userRepository.findAll();
        List<Map<String, Object>> userInfoList = new ArrayList<>();

        for (User user : users) {
            Map<String, Object> userInfoMap = new HashMap<>();
            userInfoMap.put("id", user.getId());
            userInfoMap.put("username", user.getUsername());
            userInfoMap.put("name", user.getName());
            userInfoMap.put("email", user.getEmail());
            userInfoMap.put("phonenumber", user.getPhonenumber());
            userInfoMap.put("roles", user.getRoles());
            userInfoMap.put("profile", user.getProfile());
            userInfoList.add(userInfoMap);
        }

        return userInfoList;
    }


    @Override
    public List<Venue> getVenueList() {
        return venueRepository.findAll();
    }

    @Override
    public Admin getUserNameAndPassword(String username, String password) {

        return adminRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public Admin findUserByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public String changePassword(String username, String oldPassword, String newPassword) {
        Admin admin =adminRepository.findByUsername(username);
        if (admin == null) {
            return "Admin not found";
        }

        if (!admin.getPassword().equals(oldPassword)) {
            return "Old password is incorrect";
        }

        admin.setPassword(newPassword);
        return "Password changed successfully";
    }

    @Override
    public void deleteVenue(int id) {
        adminRepository.deleteById(id);
    }
}

