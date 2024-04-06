package com.nist.venue.repository;

import com.nist.venue.entity.Devicetoken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<Devicetoken,Integer> {
    interface DeviceTokenProjection {
        String getToken();
//        Long getId();
//        String getUsername();
        // Add more getters for other fields you need
    }

    Optional<Devicetoken> findByToken(String token);


    @Query("SELECT dt.username AS username, dt.id AS id, dt.token AS token FROM Devicetoken dt WHERE dt.username = :username")
    Optional<List<DeviceTokenProjection>> findByUsername(@Param("username") String username);


    @Transactional
    @Modifying
    @Query("DELETE FROM Devicetoken dt WHERE dt.id = :tokenId")
    void deleteByTokenId(@Param("tokenId") int tokenId);

}
