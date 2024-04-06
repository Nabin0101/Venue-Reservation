package com.nist.venue.repository;

import com.nist.venue.entity.Devicetoken;
import com.nist.venue.entity.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer>{

    Optional<User> findByUsername(String username);



}
