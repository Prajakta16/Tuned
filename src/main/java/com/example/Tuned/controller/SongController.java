package com.example.Tuned.controller;

import com.example.Tuned.model.*;
import com.example.Tuned.repository.AlbumRepository;
import com.example.Tuned.repository.ListenerRepository;
import com.example.Tuned.repository.Listener_activityRepository;
import com.example.Tuned.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

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

    @GetMapping("/api/song/{song_id}/album")
    public Album findAlbumDetailsForSong(@PathVariable("song_id") int song_id){
        if (songRepository.findById(song_id).isPresent()){
            Album album = songRepository.findAlbumForSong(song_id);
            return album;
        }
        return null;
    }

    @DeleteMapping("api/song/delete/{song_id}")
    public void deleteSongById(@PathVariable("song_id") int song_id) {
        Song song = songRepository.findById(song_id).get();
        songRepository.delete(song);
    }

    @GetMapping("/api/song/liked/by/{listener_id}")
    public List<Song> getLikedSongForAUser(@PathVariable("listener_id") int listener_id) {
       List<Song> Final = newArrayList();
       if (listenerRepository.findById(listener_id).isPresent()) {
          Listener listener = listenerRepository.findById(listener_id).get();
          List<Song> song = listener_activityRepository.findSongByListener(listener);
          Iterator<Song> iter = song.iterator();
          while (iter.hasNext()) {
              Song songs = iter.next();
              Listener_activity la = listener_activityRepository.findActivityByListenerAndSong(listener,songs);
              if (la.getLikes() == Boolean.TRUE)
              {
                  Final.add(songs);
              }

          }
          return Final;
        }
       return null;
    }

    @GetMapping("/api/song/disliked/by/{listener_id}")
    public List<Song> getDislikedSongForAUser(@PathVariable("listener_id") int listener_id) {
        List<Song> Final = newArrayList();
        if (listenerRepository.findById(listener_id).isPresent()) {
            Listener listener = listenerRepository.findById(listener_id).get();
            List<Song> song = listener_activityRepository.findSongByListener(listener);
            Iterator<Song> iter = song.iterator();
            while (iter.hasNext()) {
                Song songs = iter.next();
                Listener_activity la = listener_activityRepository.findActivityByListenerAndSong(listener,songs);
                if (la.getDislikes() == Boolean.TRUE)
                {
                    Final.add(songs);
                }

            }
            return Final;
        }
        return null;
    }

    @GetMapping("/api/song/favourite/by/{listener_id}")
    public List<Song> getFavouriteSongForAUser(@PathVariable("listener_id") int listener_id) {
        List<Song> Final = newArrayList();
        if (listenerRepository.findById(listener_id).isPresent()) {
            Listener listener = listenerRepository.findById(listener_id).get();
            List<Song> song = listener_activityRepository.findSongByListener(listener);
            Iterator<Song> iter = song.iterator();
            while (iter.hasNext()) {
                Song songs = iter.next();
                Listener_activity la = listener_activityRepository.findActivityByListenerAndSong(listener,songs);
                if (la.isIs_favourite() == Boolean.TRUE)
                {
                    Final.add(songs);
                }

            }
            return Final;
        }
        return null;
    }
}