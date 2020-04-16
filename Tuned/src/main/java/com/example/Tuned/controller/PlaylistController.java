package com.example.Tuned.controller;

import com.example.Tuned.model.Playlist;
import com.example.Tuned.model.Song;
import com.example.Tuned.repository.PlaylistRepository;
import com.example.Tuned.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class PlaylistController {

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    SongRepository songRepository;

    @PostMapping("/api/playlist")
    public Playlist createPlaylist(@RequestBody Playlist playlist){
        return playlistRepository.save(playlist);
    }

    @PostMapping("/api/playlist/{playlist_id}/song/{song_id}")
    public Playlist createPlaylist(@PathVariable("playlist_id") int playlist_id, @PathVariable("song_id") int song_id){
        Playlist playlist = playlistRepository.findById(playlist_id).get();
        Song song = songRepository.findById(song_id).get();
        playlist.addSong(song);
        songRepository.save(song);
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

    @DeleteMapping("api/playlist/{playlist_id}")
    public void deletePlaylistById(@PathVariable("playlist_id") int playlist_id) {
        Playlist playlist = playlistRepository.findById(playlist_id).get();
        playlistRepository.delete(playlist);
    }
}
