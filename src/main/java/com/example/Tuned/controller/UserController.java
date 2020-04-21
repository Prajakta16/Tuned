package com.example.Tuned.controller;

import com.example.Tuned.model.*;
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

    @PostMapping("/api/user/{user_id}/follows/{other_user_id}")
    public User userFollowsOtherUser(@PathVariable("user_id") int user_id, @PathVariable("other_user_id") int other_user_id)
    {
        if(userRepository.findById(user_id).isPresent() && userRepository.findById(other_user_id).isPresent()){
            User user = userRepository.findById(user_id).get();
            User other_user = userRepository.findById(other_user_id).get();
            user.followUser(other_user);
            userRepository.save(other_user);
            return userRepository.save(user);
        }
        return null;
    }

    @PostMapping("/api/user/{user_id}/unfollows/{other_user_id}")
    public User userUnfollowsOtherUser(@PathVariable("user_id") int user_id, @PathVariable("other_user_id") int other_user_id)
    {
        if(userRepository.findById(user_id).isPresent() && userRepository.findById(other_user_id).isPresent()){
            User user = userRepository.findById(user_id).get();
            User other_user = userRepository.findById(other_user_id).get();
            user.unfollowUser(other_user);
            userRepository.save(other_user);
            return userRepository.save(user);
        }
        return null;
    }

    @GetMapping("/api/user/{user_id}/following")
    public List<User> getFollowing(@PathVariable("user_id") int user_id)
    {
        if(userRepository.findById(user_id).isPresent()){
           List<User> following = userRepository.findFollowing(user_id);
           return following;
    }
        return null;
    }

    @GetMapping("/api/user/{user_id}/followers")
    public List<User> getFollowers(@PathVariable("user_id") int user_id)
    {
        if(userRepository.findById(user_id).isPresent()){
            List<User> followers = userRepository.findFollowers(user_id);
            return followers;
        }
        return null;
    }

    @PostMapping("/api/user/{user_id}/profile")
    public User updateUserProfile(@PathVariable("user_id") int user_id, @RequestBody User updatedUser)
    {
        if(userRepository.findById(user_id).isPresent())
        {
            User user = userRepository.findById(user_id).get();
            if(updatedUser.getAddress()!= null && updatedUser.getAddress()!= user.getAddress())
                user.setAddress(updatedUser.getAddress());
            if(updatedUser.getEmail() != null && updatedUser.getEmail()!= user.getEmail())
                user.setEmail(updatedUser.getEmail());
            if(updatedUser.getPhone()!= user.getPhone()) //////
                user.setPhone(updatedUser.getPhone());
            if(updatedUser.getUsername() != null && updatedUser.getUsername() != user.getUsername())
                user.setUsername(updatedUser.getUsername());
            if(updatedUser.getPassword() != null && updatedUser.getPassword() != user.getPassword())
                user.setPassword(updatedUser.getPassword());
            if(updatedUser.getFirst_name() != null && updatedUser.getFirst_name() != user.getFirst_name())
                user.setFirst_name(updatedUser.getFirst_name());
            if(updatedUser.getLast_name() != null && updatedUser.getLast_name() != user.getLast_name())
                user.setLast_name(updatedUser.getUsername());
            return userRepository.save(user);
        }
        return null;
    }

    @DeleteMapping("/api/user/{user_id}/delete")
    public void deleteUser(@PathVariable("user_id") int user_id){
        if(userRepository.findById(user_id).isPresent())
        {
            userRepository.deleteById(user_id);
        }
    }

//    @PostMapping("/api/user/admin/update/user/{user_id}/email/{mailaddress}")
//    public void updateUserMail(@PathVariable("user_id") int user_id, @PathVariable("mailaddress") String mailaddress)
//    {
//        if(userRepository.findById(user_id).isPresent())
//        {
//            User user = userRepository.findById(user_id).get();
//            user.setEmail(mailaddress);
//            userRepository.save(user);
//
//        }
//    }
//
//    @PostMapping("/api/user/admin/update/user/{user_id}/phone/{phonenumber}")
//    public void updateUserPhone(@PathVariable("user_id") int user_id, @PathVariable("phonenumber") int phonenumber)
//    {
//        if(userRepository.findById(user_id).isPresent())
//        {
//            User user = userRepository.findById(user_id).get();
//            user.setPhone(phonenumber);
//            userRepository.save(user);
//
//        }
//    }

}
