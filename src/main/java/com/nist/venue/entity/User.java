package com.nist.venue.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class User {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public String getRoles() {
        return Roles;
    }

    public void setRoles(String roles) {
        Roles = roles;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(unique = true) // Ensure uniqueness at the database level
    private String username;

    public List<Devicetoken> getDevicetokens() {
        return devicetokens;
    }

    public void setDevicetokens(List<Devicetoken> devicetokens) {
        this.devicetokens = devicetokens;
    }

    private String name;
    private String email;
    private String profile;
    private String password;
    private  String confirmpassword;
    private  String Roles;
    private  String phonenumber;



    @Transient
    private String newpassword;







    @Transient
    private String confirmnewpassword;

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getConfirmnewpassword() {
        return confirmnewpassword;
    }

    public void setConfirmnewpassword(String confirmnewpassword) {
        this.confirmnewpassword = confirmnewpassword;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void addToken(Devicetoken token) {
        devicetokens.add(token);
        token.setUser(this);
    }

    public void removeToken(Devicetoken token) {
        devicetokens.remove(token);
        token.setUser(null);
    }


    @OneToMany(cascade = CascadeType.ALL)
    private List<Devicetoken> devicetokens=new ArrayList<>();



}
