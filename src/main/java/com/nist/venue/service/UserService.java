package com.nist.venue.service;

import com.nist.venue.entity.Devicetoken;
import com.nist.venue.entity.User;

public interface UserService {
    public User saveUser(User user);
    public User findUserById(int id);
    public void updateUser(User User);
    public void addTokenToUser(int id, Devicetoken devicetoken);
    public long getTotalUsers();

}
