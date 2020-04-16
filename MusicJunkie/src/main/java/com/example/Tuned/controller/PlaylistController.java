package com.example.Tuned.controller;

import com.example.Tuned.model.Playlist;
import com.example.Tuned.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class PlaylistController {

    @Autowired
    PlaylistRepository playlistRepository;

    @PostMapping("/api/playlist")
    public Playlist createPlaylist(@RequestBody Playlist playlist){
        return playlistRepository.save(playlist);
    }
}
