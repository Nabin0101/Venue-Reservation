package com.nist.venue.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "venue_id", referencedColumnName = "id")
    private Venue venue;


    private  Date date;


    boolean confirmStatus;

    public int getId() {
        return id;
    }
    @Transient
    private List<String> bookedDates;

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Venue getVenue() {
        return venue;
    }

    public List<String> getBookedDates() {
        return bookedDates;
    }

    public void setBookedDates(List<String> bookedDates) {
        this.bookedDates = bookedDates;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
