package com.nist.venue.repository;

import com.nist.venue.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByUsernameAndPassword(String username, String password);
    Admin findByUsername(String username);

}

