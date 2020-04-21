package com.example.Tuned.controller;

import com.example.Tuned.model.*;
import com.example.Tuned.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Service
@RestController
public class ListenerController {

    @Autowired
    ListenerRepository listenerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    Listener_activityRepository listener_activityRepository;

    @PostMapping("/api/listener/new")
    public Listener createListener(@RequestBody  Listener listener){
        listenerRepository.save(listener);
        User user = userRepository.findById(listener.getUser_id()).get();
        user.setUser_type("listener");
        userRepository.save(user);
        return listener;
    }

    @PostMapping("/api/listener/{listener_id}/playlist/new")
    public Listener addPlaylistToListener(@PathVariable("listener_id") int listener_id, @RequestBody Playlist newPlaylist){
        Listener listener = listenerRepository.findById(listener_id).get();
        listener.addPlaylist(newPlaylist);
        playlistRepository.save(newPlaylist);
        return listenerRepository.save(listener);
    }

    @GetMapping("/api/listener/all")
    public List<Listener> getAllListeners(){
        return (List<Listener>) listenerRepository.findAll();
    }

    @GetMapping("/api/listener/{listener_id}")
    public Listener getListenerById(@PathVariable("listener_id") int listener_id) {
    Listener listener = listenerRepository.findById(listener_id).get();
    List<Listener_activity> activities = listener.getListener_activities();

    Iterator<Listener_activity> iter = activities.iterator();
    String user_name = null;
    while (iter.hasNext()) {
      Listener_activity activity = (Listener_activity) iter.next();
      User user = activity.getListener();
      System.out.println(user);
      user_name = user.getUsername();
      activity.setUsername(user_name);
    }
    return listener;
    }

    @GetMapping("/api/listener/{listener_id}/playlists/all")
    public List<Playlist> findAllPlaylistsForListener(@PathVariable("listener_id") int listener_id) {
        if (listenerRepository.findById(listener_id).isPresent()) {
            Listener listener = listenerRepository.findById(listener_id).get();
            return listener.getPlaylists();
        }
        return null;
    }

    @GetMapping("/api/listener/{listener_id}/activities/all")
    public List<Listener_activity> findAllActivitiesForListener(@PathVariable("listener_id") int listener_id){
        if (listenerRepository.findById(listener_id).isPresent()) {
            Listener listener = listenerRepository.findById(listener_id).get();
            return listener_activityRepository.findActivityByListener(listener);
        }
        return null;
    }

    //Admin
    @DeleteMapping("/api/listener/delete/{listener_id}")
    public void deleteListenerById(@PathVariable("listener_id") int listener_id) {
        Listener listener = listenerRepository.findById(listener_id).get();

        List<Playlist> playlists = listener.getPlaylists();
        for(Playlist p : playlists){
            listener.removePlaylist(p);
            playlistRepository.save(p);
        }
        listenerRepository.save(listener);

        User user = userRepository.findById(listener_id).get();
        user.removeAllFollowersAndFollowing();
        userRepository.save(user);

        listenerRepository.deleteById(listener_id);
        if(userRepository.findById(listener_id).isPresent())
            userRepository.deleteById(listener_id);
    }
}
