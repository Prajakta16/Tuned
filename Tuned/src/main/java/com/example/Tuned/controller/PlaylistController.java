package com.example.Tuned.controller;

import com.example.Tuned.model.Playlist;
import com.example.Tuned.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class PlaylistController {

    @Autowired
    PlaylistRepository playlistRepository;

    @PostMapping("/api/playlist")
    public Playlist createPlaylist(@RequestBody Playlist playlist){
        return playlistRepository.save(playlist);
    }

    @GetMapping("/api/playlist/{playlist_id}")
    public Playlist findPlaylistById(@PathVariable("playlist_id") int playlist_id) {
        if (playlistRepository.findById(playlist_id).isPresent())
            return playlistRepository.findById(playlist_id).get();
        else
            return null;
    }

    @GetMapping("/api/playlists/all")
    public List<Playlist> findAllPlaylists() {
        return (List<Playlist>) playlistRepository.findAll();
    }
}
