package com.example.MusicJunkie.dao;

import com.example.MusicJunkie.model.User;
import com.example.MusicJunkie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDao {

    @Autowired
    UserRepository userRepository;

}
