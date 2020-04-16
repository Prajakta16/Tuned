package com.example.Tuned.controller;

import com.example.Tuned.model.Listener;
import com.example.Tuned.model.User;
import com.example.Tuned.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Service
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/api/users/all")
    public List<User> getAllUsers(){
        return (List<User>) userRepository.findAll();
    }

    @GetMapping("api/users/{user_id}")
    public User getUserById(@PathVariable("user_id") int user_id) {
        return userRepository.findById(user_id).get();
    }

    @GetMapping("api/users/{first_name}")
    public User getUserByFirst_name(@PathVariable("first_name") String first_name) {
        return userRepository.findUserByFirst_name(first_name);
    }
}
