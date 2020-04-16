package com.example.Tuned.dao;

import com.example.Tuned.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDao {

    @Autowired
    UserRepository userRepository;

}
