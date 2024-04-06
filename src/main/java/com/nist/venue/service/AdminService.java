package com.nist.venue.service;

import com.nist.venue.entity.Admin;
import com.nist.venue.entity.User;
import com.nist.venue.entity.Venue;

import java.util.List;
import java.util.Map;

public interface AdminService {
    public  void saveAdmin(Admin admin);
    public void updateAdmin(Admin admin, String username);
    public void deleteAdmin(int id);
    List<Map<String, Object>> getUserInfo();
    public List<Venue> getVenueList();
    public Admin getUserNameAndPassword(String username, String password);
    Admin findUserByUsername(String username);
    public String changePassword(String username,String oldPassword, String newPassword);
    public void deleteVenue(int id);
}
