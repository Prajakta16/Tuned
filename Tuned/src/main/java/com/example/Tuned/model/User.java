package com.example.Tuned.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity(name = "user")
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column(unique = true)
    private String username;
    //@Column(nullable = false)
    private String password;

    private String first_name;
    private String last_name;

    @Column(unique = true)
    private int phone;
    private String address;
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "follower_detail",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "user_id"))
    @JsonIgnore
    private List<User> followers;

    @ManyToMany(mappedBy = "followers", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> follows;


    public User(String username, String password, String first_name, String last_name, int phone, String address, String email) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    public User() {

    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getFollows() {
        return follows;
    }

    public void setFollows(List<User> follows) {
        this.follows = follows;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addFollower(User followers){
        this.followers.add(followers);
        if(!followers.getFollowers().contains(this))
            followers.getFollowers().add(this);
    }

    public void removeFollower(User followers){
        this.followers.remove(followers);
        if(followers.getFollowers().contains(this))
            followers.getFollowers().remove(this);
    }


}
