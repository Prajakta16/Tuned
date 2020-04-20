package com.example.Tuned.controller;

import com.example.Tuned.model.*;
import com.example.Tuned.repository.ListenerRepository;
import com.example.Tuned.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Service
@RestController
public class ListenerController {

    @Autowired
    ListenerRepository listenerRepository;

    @Autowired
    PlaylistRepository playlistRepository;

    @PostMapping("/api/listener/new")
    Listener createListener(@RequestBody  Listener listener){
        return listenerRepository.save(listener);
    }

    @PostMapping("/api/listener/{listener_id}/playlist/new")
    Listener addPlaylistToListener(@PathVariable("listener_id") int listener_id, @RequestBody Playlist newPlaylist){
        Listener listener = listenerRepository.findById(listener_id).get();
        listener.addPlaylist(newPlaylist);
        playlistRepository.save(newPlaylist);
        return listenerRepository.save(listener);
    }

    @GetMapping("/api/listener/all")
    public List<Listener> getAllListeners(){
        return (List<Listener>) listenerRepository.findAll();
    }

    //GET THE USERNAME OF THE USER-ACTIVITY
    //I tried fetching username but couldn't display the user name in listener activity
    //So I tried adding a new column user_name in listener_activity
    //But the problem is, as we are finding listener based on listener_id, only his username is displayed in the field user_name of listener activity
    @GetMapping("/api/listener/find/{listener_id}")
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

    //Admin
    @DeleteMapping("/api/listener/delete/{listener_id}")
    public void deleteListenerById(@PathVariable("listener_id") int listener_id) {
        Listener listener = listenerRepository.findById(listener_id).get();
        listenerRepository.delete(listener);
    }
}
