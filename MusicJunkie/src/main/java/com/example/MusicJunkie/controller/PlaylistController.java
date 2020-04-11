package com.example.MusicJunkie.controller;

import com.example.MusicJunkie.model.Playlist;
import com.example.MusicJunkie.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PlaylistController {

    @Autowired
    PlaylistRepository playlistRepository;

    public Playlist createPlaylist(Playlist playlist){
        return playlistRepository.save(playlist);
    }
}
