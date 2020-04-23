package com.example.Tuned.controller;

import com.example.Tuned.model.*;
import com.example.Tuned.repository.*;
import org.json.simple.JSONObject;
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
    public JSONObject createListener(@RequestBody Listener listener) {
        listener.setUser_type("listener");
        Listener listener1 = listenerRepository.findListenerByUserName(listener.getUsername());
        JSONObject jsonObject = new JSONObject();
        if(listener1!=null){
            jsonObject.put("error", "Username "+ listener1.getUsername() +" already exists.");
            return jsonObject;
        }

        try {
            Listener newL = listenerRepository.save(listener);
            jsonObject.put("success", "success");
            jsonObject.put("user_id", newL.getUser_id());
            return jsonObject;
        }catch (Exception e){
            jsonObject.put("error", "Some error occurred");
            return jsonObject;
        }



        //
    }



    @PostMapping("/api/listener/{listener_id}/playlist/new")
    public Listener addPlaylistToListener(@PathVariable("listener_id") int listener_id, @RequestBody Playlist newPlaylist) {
        if(listenerRepository.findById(listener_id).isPresent()) {
            Listener listener = listenerRepository.findById(listener_id).get();
            listener.addPlaylist(newPlaylist);
            playlistRepository.save(newPlaylist);
            return listenerRepository.save(listener);
        }
        return null;
    }

    @PostMapping("/api/listener/{listener_id}/playlist/{playlist_id}/remove")
    public Listener removePlaylistFromListener(@PathVariable("listener_id") int listener_id, @PathVariable("playlist_id") int playlist_id) {
        if(listenerRepository.findById(listener_id).isPresent()) {
            Playlist playlist = playlistRepository.findById(playlist_id).get();
            playlistRepository.delete(playlist);
            return listenerRepository.findListenerByUserId(listener_id);
        }
        return null;
    }

    @GetMapping("/api/listener/all")
    public List<Listener> getAllListeners() {
        return (List<Listener>) listenerRepository.findAll();
    }

    @GetMapping("/api/listener/{listener_id}")
    public Listener getListenerById(@PathVariable("listener_id") int listener_id) {
        if (listenerRepository.findById(listener_id).isPresent()) {
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
        return null;
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
    public List<Listener_activity> findAllActivitiesForListener(@PathVariable("listener_id") int listener_id) {
        if (listenerRepository.findById(listener_id).isPresent()) {
            Listener listener = listenerRepository.findById(listener_id).get();
            return listener_activityRepository.findActivityByListener(listener);
        }
        return null;
    }


}
