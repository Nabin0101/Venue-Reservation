package com.nist.venue.service;

import com.nist.venue.entity.Devicetoken;
import com.nist.venue.entity.User;
import com.nist.venue.repository.DeviceTokenRepository;
import com.nist.venue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceTokenServiceImpl implements DeviceTokenService {

    @Autowired
    DeviceTokenRepository deviceTokenRepository;


    @Autowired
    UserRepository userRepository;


}
