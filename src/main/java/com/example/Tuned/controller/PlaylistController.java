package com.example.Tuned.controller;

import com.example.Tuned.model.*;
import com.example.Tuned.repository.ListenerRepository;
import com.example.Tuned.repository.PlaylistRepository;
import com.example.Tuned.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Service
public class PlaylistController {

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    ListenerRepository listenerRepository;

    //Admin purpose PD-this would be irrelevant because a playlist cannot exists without a listener
    @PostMapping("/api/playlist/new")
    public Playlist createPlaylist(@RequestBody Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    @PostMapping("/api/playlist/{playlist_id}/song/{song_id}")
    public Playlist addSongToPlaylist(@PathVariable("playlist_id") int playlist_id, @PathVariable("song_id") int song_id) {
        if (playlistRepository.findById(playlist_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Playlist playlist = playlistRepository.findById(playlist_id).get();
            Song song = songRepository.findById(song_id).get();
            playlist.addSong(song);
            songRepository.save(song);
            return playlistRepository.save(playlist);
        }
        return null;
    }

    @PostMapping("/api/playlist/{playlist_id}/song/{song_id}/remove")
    public Playlist removeSongFromPlaylist(@PathVariable("playlist_id") int playlist_id, @PathVariable("song_id") int song_id) {
        if (playlistRepository.findById(playlist_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Playlist playlist = playlistRepository.findById(playlist_id).get();
            Song song = songRepository.findById(song_id).get();

            playlist.removeSong(song);
            songRepository.save(song);
            return playlistRepository.save(playlist);
        }
        return null;
    }

    @GetMapping("/api/playlist/{playlist_id}")
    public Playlist findPlaylistById(@PathVariable("playlist_id") int playlist_id) {
        if (playlistRepository.findById(playlist_id).isPresent())
            return playlistRepository.findById(playlist_id).get();
        else
            return null;
    }

    @GetMapping("/api/playlist/{playlist_id}/listener")
    public Listener getListenerDetailsForPlaylist(@PathVariable("playlist_id") int playlist_id) {
        if (playlistRepository.findById(playlist_id).isPresent())
            return playlistRepository.findListenerDetails(playlist_id);
        return null;
    }

    @GetMapping("/api/playlists/all")
    public List<Playlist> findAllPlaylists() {
        return (List<Playlist>) playlistRepository.findAll();
    }

    //Admin and also listener
    @DeleteMapping("/api/playlist/delete/{playlist_id}")
    public void deletePlaylistById(@PathVariable("playlist_id") int playlist_id) {
        Playlist playlist = playlistRepository.findById(playlist_id).get();

        Set<Song> songs = playlist.getSongs();
        if (songs != null)
            for (Song s : songs)
                removeSongFromPlaylist(playlist_id, s.getSong_id());

        ListenerController listenerController = new ListenerController();
        Listener listener = playlist.getListener();
        if(listener!=null)
//            listenerController.removePlaylistFromListener(listener.getUser_id(),playlist_id);

        playlistRepository.delete(playlist);
    }
}
