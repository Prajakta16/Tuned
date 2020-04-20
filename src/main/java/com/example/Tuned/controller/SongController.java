package com.example.Tuned.controller;

import com.example.Tuned.model.Listener;
import com.example.Tuned.model.Listener_activity;
import com.example.Tuned.model.Song;
import com.example.Tuned.repository.AlbumRepository;
import com.example.Tuned.repository.ListenerRepository;
import com.example.Tuned.repository.Listener_activityRepository;
import com.example.Tuned.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Service
public class SongController {

    @Autowired
    SongRepository songRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ListenerRepository listenerRepository;

    @Autowired
    Listener_activityRepository listener_activityRepository;

    @PostMapping("/api/song/new")
    public Song createSong(@RequestBody Song song){
        return songRepository.save(song);
    }

    @GetMapping("/api/song/all")
    public List<Song> findAllSongs() {
        return (List<Song>) songRepository.findAll();
    }

    @GetMapping("/api/song/{song_id}")
    public Song findSongById(@PathVariable("song_id") int song_id) {
        if (songRepository.findById(song_id).isPresent())
            return songRepository.findById(song_id).get();
        else
            return null;
    }
    @GetMapping("/api/song/name/{title}")
    public List<Song> findSongByTitle(@PathVariable("title") String title) {
        return songRepository.findSongByTitle(title);
    }

    //@GetMapping("api/song/{album_id}")
    //public List<Song> findSongByAlbum(@PathVariable("album_id") int album_id) {
    //         return (List<Song>) songRepository.findSongByAlbum(album_id);
    //}

    //@GetMapping("api/song/{user_id}")
    //public List<Song> findSongByArtist(@PathVariable("user_id") int user_id){
    //    return (List<Song>) songRepository.findSongByArtist(user_id);
    //}

    @DeleteMapping("api/song/{song_id}")
    public void deleteSongById(@PathVariable("song_id") int song_id) {
        Song song = songRepository.findById(song_id).get();
        songRepository.delete(song);
    }

    @GetMapping("/api/song/liked/by/{listener_id}")
    public List<Song> getLikedSongForAUser(@PathVariable("listener_id") int listener_id) {
        if (listenerRepository.findById(listener_id).isPresent()) {
            Listener_activity la;
            Listener listener = listenerRepository.findById(listener_id).get();
            if(listener_activityRepository.findSongByListener(listener)!= null) {
                la = listener_activityRepository.findById(listener_id).get();
                if (la.getLikes() == true) {
                    List<Song> song = (List<Song>) listener_activityRepository.findSongByListener(listener);
                    return song;
                }
            }
        }
        return null;
    }
}
