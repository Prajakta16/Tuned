package com.example.Tuned.controller;

import com.example.Tuned.model.Listener;
import com.example.Tuned.model.Listener_activity;
import com.example.Tuned.model.Song;
import com.example.Tuned.model.User;
import com.example.Tuned.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Service
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/api/users/all")
    public List<User> getAllUsers(){
        return (List<User>) userRepository.findAll();
    }

    @GetMapping("/api/users/id/{user_id}")
    public User getUserById(@PathVariable("user_id") int user_id) {
        return userRepository.findById(user_id).get();
    }

    @GetMapping("/api/users/name/{first_name}")
    public User getUserByFirst_name(@PathVariable("first_name") String first_name) {
        return userRepository.findUserByFirst_name(first_name);
    }

    @GetMapping("/api/users/username/{username}")
    public User getUserByUsername(@PathVariable("username") String username) {
        return userRepository.findUserByUsername(username);
    }

    @PostMapping("/api/users/{user_id}/follows/{follower_id}")
    public void userFollows(@PathVariable("user_id") int user_id, @PathVariable("follower_id") int follower_id)
    {
        User user = userRepository.findById(user_id).get();
        User follower = userRepository.findById(follower_id).get();
        user.followUser(follower);
        userRepository.save(follower);
    }

    @GetMapping("/api/users/{user_id}/getfollowing")
    public User listuserFollows(@PathVariable("user_id") int user_id)
    {
        User user = userRepository.findById(user_id).get();
        user.getFollowers();
        return userRepository.save(user);
    }

    @GetMapping("/api/users/{user_id}/getfollower")
    public User listuserFollower(@PathVariable("user_id") int user_id)
    {
        User user = userRepository.findById(user_id).get();
        user.getFollows();
        return userRepository.save(user);
    }

    @PostMapping("/api/users/{user_id}/unfollows/{follower_id}")
    public void userUnfollows(@PathVariable("user_id") int user_id, @PathVariable("follower_id") int follower_id) {
        User user = userRepository.findById(user_id).get();
        User follower = userRepository.findById(follower_id).get();
        user.unfollowUser(follower);
        userRepository.save(user);
    }

}
