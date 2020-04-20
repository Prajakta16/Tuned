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

//
//    @GetMapping("/api/users/{user_id}/getfollowing")
//    public User listuserFollows(@PathVariable("user_id") int user_id)
//    {
//        User user = userRepository.findById(user_id).get();
//        user.getFollowers();
//        return userRepository.save(user);
//    }
//
//    @GetMapping("/api/users/{user_id}/getfollower")
//    public User listuserFollower(@PathVariable("user_id") int user_id)
//    {
//        User user = userRepository.findById(user_id).get();
//        user.getFollows();
//        return userRepository.save(user);
//    }
//
//    @PostMapping("/api/users/{user_id}/unfollows/{follower_id}")
//    public void userUnfollows(@PathVariable("user_id") int user_id, @PathVariable("follower_id") int follower_id) {
//        User user = userRepository.findById(user_id).get();
//        User follower = userRepository.findById(follower_id).get();
//        user.unfollowUser(follower);
//        userRepository.save(user);
//    }
//
//    //show if he already follows
//    @PostMapping("/api/users/{user_id}/checkfollows/{follower_id}")
//    public boolean checkUserFollows(@PathVariable("user_id") int user_id, @PathVariable("follower_id") int follower_id)
//    {
//        User user = userRepository.findById(user_id).get();
//        User follower = userRepository.findById(follower_id).get();
//        List<User> followers = user.getFollowers();
//        if (followers.contains(follower))
//        {
//            return Boolean.TRUE;
//        }
//        else
//        {
//            return Boolean.FALSE;
//        }
//    }
    
    //Updates for Admin
    @PostMapping("/api/user/admin/update/user/{user_id}/address/{addressname}")
    public void updateUserAddress(@PathVariable("user_id") int user_id, @PathVariable("addressname") String addressname)
    {
        if(userRepository.findById(user_id).isPresent())
        {
            User user = userRepository.findById(user_id).get();
            user.setAddress(addressname);
            userRepository.save(user);

        }
    }

    @PostMapping("/api/user/admin/update/user/{user_id}/email/{mailaddress}")
    public void updateUserMail(@PathVariable("user_id") int user_id, @PathVariable("mailaddress") String mailaddress)
    {
        if(userRepository.findById(user_id).isPresent())
        {
            User user = userRepository.findById(user_id).get();
            user.setEmail(mailaddress);
            userRepository.save(user);

        }
    }

    @PostMapping("/api/user/admin/update/user/{user_id}/phone/{phonenumber}")
    public void updateUserPhone(@PathVariable("user_id") int user_id, @PathVariable("phonenumber") int phonenumber)
    {
        if(userRepository.findById(user_id).isPresent())
        {
            User user = userRepository.findById(user_id).get();
            user.setPhone(phonenumber);
            userRepository.save(user);

        }
    }

}
