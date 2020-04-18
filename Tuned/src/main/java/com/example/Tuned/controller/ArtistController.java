package com.example.Tuned.controller;

import com.example.Tuned.model.Artist;
import com.example.Tuned.model.Song;
import com.example.Tuned.repository.ArtistRepository;
import com.example.Tuned.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Service
public class ArtistController {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    SongRepository songRepository;

    @PostMapping("/api/artist")
    public Artist createArtist(@RequestBody Artist artist){
        return artistRepository.save(artist);
    }

    @GetMapping("/api/artist/all")
    public List<Artist> getAllArtists(){
        return (List<Artist>) artistRepository.findAll();
    }

    @GetMapping("api/artist/{first_name}")
    public Artist findArtistByFirstName(@PathVariable("first_name") String first_name){
        return artistRepository.findArtistByFirstName(first_name);
    }

//    @PostMapping("/api/artist/{artist_id}/song/new")
//    Artist addSongToArtist(@PathVariable("artist_id") int artist_id, @RequestBody Song newSong){
//        Artist artist = artistRepository.findById(artist_id).get();
//        artist.addSongs(newSong);
//        songRepository.save(newSong);
//        return artistRepository.save(artist);
//    }
//
//    @GetMapping("api/artist/{artist_id}/songs/all")
//    public List<Song> findSongOfArtist(@PathVariable("artist_id") int artist_id) {
//        if (artistRepository.findById(artist_id).isPresent()) {
//            Artist artist = artistRepository.findById(artist_id).get();
//            return artist.getProducedSongs();
//        }
//        return null;
//    }
}
