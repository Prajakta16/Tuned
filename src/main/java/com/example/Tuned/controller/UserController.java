package com.example.Tuned.controller;

import com.example.Tuned.model.*;
import com.example.Tuned.repository.ArtistRepository;
import com.example.Tuned.repository.ListenerRepository;
import com.example.Tuned.repository.PlaylistRepository;
import com.example.Tuned.repository.UserRepository;
import org.json.simple.JSONObject;
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

    @Autowired
    PlaylistRepository playlistRepository;

    @GetMapping("/api/users/all")
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @GetMapping("/api/user/isValidUser")
    public Boolean verifyLoginByCredentials(@RequestBody User user) {
        if(userRepository.findUserByUsername(user.getUsername())!=null){
            String username = user.getUsername();
            String password = user.getPassword();
            if(userRepository.findUserByCredentials(username,password)!=null)
                return true;
            else return false;
        }
        return false;
    }

    @PostMapping("/api/user/{user_id}/follows/{other_user_id}")
    public User userFollowsOtherUser(@PathVariable("user_id") int user_id, @PathVariable("other_user_id") int other_user_id) {
        if (userRepository.findById(user_id).isPresent() && userRepository.findById(other_user_id).isPresent()) {
            User user = userRepository.findById(user_id).get();
            User other_user = userRepository.findById(other_user_id).get();
            user.followUser(other_user);
            userRepository.save(other_user);
            return userRepository.save(user);
        }
        return null;
    }

    @PostMapping("/api/user/{user_id}/unfollows/{other_user_id}")
    public User userUnfollowsOtherUser(@PathVariable("user_id") int user_id, @PathVariable("other_user_id") int other_user_id) {
        if (userRepository.findById(user_id).isPresent() && userRepository.findById(other_user_id).isPresent()) {
            User user = userRepository.findById(user_id).get();
            User other_user = userRepository.findById(other_user_id).get();
            user.unfollowUser(other_user);
            userRepository.save(other_user);
            return userRepository.save(user);
        }
        return null;
    }

    @GetMapping("/api/user/{user_id}/following")
    public List<User> getFollowing(@PathVariable("user_id") int user_id) {
        if (userRepository.findById(user_id).isPresent()) {
            List<User> following = userRepository.findFollowing(user_id);
            return following;
        }
        return null;
    }

    @GetMapping("/api/user/{user_id}/followers")
    public List<User> getFollowers(@PathVariable("user_id") int user_id) {
        if (userRepository.findById(user_id).isPresent()) {
            List<User> followers = userRepository.findFollowers(user_id);
            return followers;
        }
        return null;
    }

    @PostMapping("/api/user/{user_id}/profile")
    public User updateUserProfile(@PathVariable("user_id") int user_id, @RequestBody User updatedUser) {
        if (userRepository.findById(user_id).isPresent()) {
            User user = userRepository.findById(user_id).get();
            if (updatedUser.getAddress() != null && updatedUser.getAddress() != user.getAddress())
                user.setAddress(updatedUser.getAddress());
            if (updatedUser.getEmail() != null && updatedUser.getEmail() != user.getEmail())
                user.setEmail(updatedUser.getEmail());
            if (updatedUser.getPhone() != user.getPhone()) //////
                user.setPhone(updatedUser.getPhone());
            if (updatedUser.getUsername() != null && updatedUser.getUsername() != user.getUsername())
                user.setUsername(updatedUser.getUsername());
            if (updatedUser.getPassword() != null && updatedUser.getPassword() != user.getPassword())
                user.setPassword(updatedUser.getPassword());
            if (updatedUser.getFirst_name() != null && updatedUser.getFirst_name() != user.getFirst_name())
                user.setFirst_name(updatedUser.getFirst_name());
            if (updatedUser.getLast_name() != null && updatedUser.getLast_name() != user.getLast_name())
                user.setLast_name(updatedUser.getLast_name());
            return userRepository.save(user);
        }
        return null;
    }

    public User removeAllFollowersAndFollowingInfo(int user_id) {
        if (userRepository.findById(user_id).isPresent()) {
            User user = userRepository.findById(user_id).get();
            user.removeAllFollowersAndFollowing();
            return userRepository.save(user);
        }
        return null;
    }

    //duplicate API's. Only delete user is enough, remove them from code if you are not using API url it on UI

    @DeleteMapping("/api/user/delete/{user_id}")
    public JSONObject deleteUserById(@PathVariable("user_id") int user_id) {
        if (userRepository.findById(user_id).isPresent()) {
            User user = userRepository.findById(user_id).get();

            List<User> following = user.getFollowing();
            while(!following.isEmpty()){
                User f = following.get(0);
                user.unfollowUser(f);
                userRepository.save(user);
            }
            JSONObject jsonObject = new JSONObject();
            try{
                userRepository.delete(user);
                if(!userRepository.findById(user_id).isPresent())
                    jsonObject.put("Success", "true");
                else
                    jsonObject.put("Success", "false");
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
                jsonObject.put("error" , "cannot delete user");
                return jsonObject;
            }
        }
        return null;
    }

    @DeleteMapping("/api/artist/delete/{artist_id}")
    public JSONObject deleteArtist(@PathVariable("artist_id") int artist_id) {
        if (userRepository.findById(artist_id).isPresent()) {
            User user = userRepository.findById(artist_id).get();

            List<User> following = user.getFollowing();
            for(User f : following){
                user.unfollowUser(f);
                userRepository.save(user);
            }
            userRepository.delete(user);

            JSONObject jsonObject = new JSONObject();
            try{
                userRepository.delete(user);
                if(!userRepository.findById(artist_id).isPresent())
                    jsonObject.put("Success", "true");
                else
                    jsonObject.put("Success", "false");
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
                jsonObject.put("error" , "cannot delete user");
                return jsonObject;
            }
        }
        return null;
    }

    @DeleteMapping("/api/listener/delete/{listener_id}")
    public JSONObject deleteListener(@PathVariable("listener_id") int listener_id) {
        if (userRepository.findById(listener_id).isPresent()) {
            User user = userRepository.findById(listener_id).get();

            List<User> following = user.getFollowing();
            for(User f : following){
                user.unfollowUser(f);
                userRepository.save(user);
            }
            userRepository.delete(user);

            JSONObject jsonObject = new JSONObject();
            try{
                userRepository.delete(user);
                if(!userRepository.findById(listener_id).isPresent())
                    jsonObject.put("Success", "true");
                else
                    jsonObject.put("Success", "false");
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
                jsonObject.put("error" , "cannot delete user");
                return jsonObject;
            }
        }
        return null;
    }

}

