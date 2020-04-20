package com.example.Tuned.controller;

import com.example.Tuned.model.Album;
import com.example.Tuned.model.Artist;
import com.example.Tuned.model.Song;
import com.example.Tuned.repository.AlbumRepository;
import com.example.Tuned.repository.ArtistRepository;
import com.example.Tuned.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Service
@RestController
public class ArtistController {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    AlbumRepository albumRepository;

    @PostMapping("/api/artist/new")
    Artist createArtist(@RequestBody Artist artist){
        return artistRepository.save(artist);
    }

    @GetMapping("/api/artist/all")
    public List<Artist> getAllArtists(){
        return (List<Artist>) artistRepository.findAll();
    }

    @GetMapping("/api/artist/{artist_id}")
    public Artist findArtistById(@PathVariable("artist_id") int artist_id){
        return artistRepository.findById(artist_id).get();
    }

    @PostMapping("/api/artist/{artist_id}/album/{album_id}")
    public Artist addAlbumToArtist(@PathVariable("artist_id") int artist_id, @PathVariable("album_id") int album_id) {
        if (artistRepository.findById(artist_id).isPresent() && albumRepository.findById(album_id).isPresent()) {
            Artist artist = artistRepository.findById(artist_id).get();
            Album album = albumRepository.findById(album_id).get();
            artist.addAlbum(album);
            albumRepository.save(album);
            return artistRepository.save(artist);
        }
        return null;
    }

    //    @PostMapping("/api/artist/{artist_id}/song/new")
//    Artist addSongToArtist(@PathVariable("artist_id") int artist_id, @RequestBody Song newSong){
//        Artist artist = artistRepository.findById(artist_id).get();
//        artist.addSongs(newSong);
//        songRepository.save(newSong);
//        return artistRepository.save(artist);
//    }
//
    @GetMapping("/api/artist/{artist_id}/albums/all")
    public Set<Album> findAlbumOfArtist(@PathVariable("artist_id") int artist_id) {
        if (artistRepository.findById(artist_id).isPresent()) {
            Artist artist = artistRepository.findById(artist_id).get();
            return artist.getProducedAlbums();
        }
        return null;
    }
}
