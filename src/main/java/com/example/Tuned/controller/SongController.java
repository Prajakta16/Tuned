package com.example.Tuned.controller;

import com.example.Tuned.model.*;
import com.example.Tuned.repository.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


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

    @Autowired
    PlaylistRepository playlistRepository;

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
            return songRepository.findAlbumForSong(song_id);
        }
        return null;
    }

    @DeleteMapping("/api/song/delete/{song_id}")
    public JSONObject deleteSongById(@PathVariable("song_id") int song_id) {
        if(songRepository.findById(song_id).isPresent()) {
            Song song = songRepository.findById(song_id).get();
            PlaylistController playlistController = new PlaylistController();
            Set<Playlist> playlists = song.getPlaylists();
            if (playlists != null)
                for (Playlist p : playlists) {
                    p.removeSong(song);
                    songRepository.save(song);
                    playlistRepository.save(p);


                }
            Album album = song.getAlbum();
            if (album != null){
                Album albumN = albumRepository.findById(album.getAlbum_id()).get();

                albumN.removeSong(song);
                songRepository.save(song);
                albumRepository.save(album);

            }



            songRepository.delete(song);
            JSONObject jsonObject = new JSONObject();
            if (!songRepository.findById(song_id).isPresent())
                jsonObject.put("Success", true);
            else
                jsonObject.put("Success", false);
            return jsonObject;
        }
        return null;
    }

}