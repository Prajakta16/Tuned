package com.example.Tuned.controller;

import com.example.Tuned.model.Artist;
import com.example.Tuned.model.Listener;
import com.example.Tuned.model.Playlist;
import com.example.Tuned.repository.ListenerRepository;
import com.example.Tuned.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/api/listener/{listener_id}")
    public Listener getListenerById(@PathVariable("listener_id") int listener_id){
        return listenerRepository.findById(listener_id).get();
    }

    @GetMapping("/api/listener/{listener_id}/playlists/all")
    public List<Playlist> findAllPlaylistsForListener(int listener_id) {
        if (listenerRepository.findById(listener_id).isPresent()) {
            Listener listener = listenerRepository.findById(listener_id).get();
            return listener.getPlaylists();
        }
        return null;
    }
}
