package com.example.MusicJunkie.controller;

import com.example.MusicJunkie.model.Playlist;
import com.example.MusicJunkie.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Service
public class PlaylistController {

    @Autowired
    PlaylistRepository playlistRepository;

    @PostMapping("/api/playlist")
    public Playlist createPlaylist(@RequestBody Playlist playlist){
        return playlistRepository.save(playlist);
    }
}
