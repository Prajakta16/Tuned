package com.example.Tuned.controller;

import com.example.Tuned.model.*;
import com.example.Tuned.repository.ArtistRepository;
import com.example.Tuned.repository.ListenerRepository;
import com.example.Tuned.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Service
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ListenerRepository listenerRepository;

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
            User user = userRepository.findById(user_id).get();

            List<User> following = user.getFollowing();
            for(User f : following){
                userUnfollowsOtherUser(user_id,f.getUser_id());
            }

            List<User> followers = user.getFollowers();
            for(User f : followers){
                userUnfollowsOtherUser(f.getUser_id(),user_id);
            }

            if(artistRepository.findArtistByUserId(user_id) != null){
                ArtistController artistController = new ArtistController();
                artistController.deleteArtistById(user_id);
            }
            else{
                if(listenerRepository.findListenertByUserId(user_id) != null){
                    ////
                }
            }

            userRepository.deleteById(user_id);
        }
    }

}
