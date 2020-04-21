package com.example.Tuned.controller;

import com.example.Tuned.model.Album;
import com.example.Tuned.model.Artist;
import com.example.Tuned.model.Playlist;
import com.example.Tuned.model.Song;
import com.example.Tuned.repository.AlbumRepository;
import com.example.Tuned.repository.ArtistRepository;
import com.example.Tuned.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
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

    @PostMapping("/api/artist/{artist_id}/new/album")
    Artist createNewAlbumForArtist(@PathVariable("artist_id") int artist_id,@RequestBody Album album ){
        if(artistRepository.findById(artist_id).isPresent()) {
            Artist artist = artistRepository.findById(artist_id).get();
            artist.addAlbum(album);
            albumRepository.save(album);
            return artistRepository.save(artist);
        }
        return null;
    }

    @PostMapping("/api/artist/{artist_id}/album/{album_id}/remove")
    public Artist removeAlbumFromArtist(@PathVariable("album_id") int album_id, @PathVariable("artist_id") int artist_id) {
        if (albumRepository.findById(album_id).isPresent() && artistRepository.findById(artist_id).isPresent()) {
            Album album = albumRepository.findById(album_id).get();
            Artist artist = artistRepository.findById(artist_id).get();

            artist.removeAlbumFromArtist(album);
            albumRepository.save(album);
            return artistRepository.save(artist);
        }
        return null;
    }

    @DeleteMapping("/api/artist/{artist_id}/delete")
    public void deleteArtist(@PathVariable("artist_id") int artist_id){
        if ( artistRepository.findById(artist_id).isPresent()) {
            Artist artist = artistRepository.findById(artist_id).get();
            Set<Album> albums = artist.getProducedAlbums();

            for(Album a : albums){
                removeAlbumFromArtist(a.getAlbum_id(),artist_id);
            }

            artistRepository.deleteById(artist_id);
        }
    }

    @PostMapping("/api/artist/{artist_id}/album/{album_id}")
    public Artist addExistingAlbumToArtist(@PathVariable("artist_id") int artist_id, @PathVariable("album_id") int album_id) {
        if (artistRepository.findById(artist_id).isPresent() && albumRepository.findById(album_id).isPresent()) {
            Artist artist = artistRepository.findById(artist_id).get();
            Album album = albumRepository.findById(album_id).get();
            artist.addAlbum(album);
            albumRepository.save(album);
            return artistRepository.save(artist);
        }
        return null;
    }

    @GetMapping("/api/artist/all")
    public List<Artist> getAllArtists(){
        return (List<Artist>) artistRepository.findAll();
    }

    @GetMapping("/api/artist/{artist_id}")
    public Artist findArtistById(@PathVariable("artist_id") int artist_id){
        return artistRepository.findById(artist_id).get();
    }

    @GetMapping("/api/artist/{artist_id}/albums/all")
    public Set<Album> findAlbumOfArtist(@PathVariable("artist_id") int artist_id) {
        if (artistRepository.findById(artist_id).isPresent()) {
            Artist artist = artistRepository.findById(artist_id).get();
            return artist.getProducedAlbums();
        }
        return null;
    }

    //Admin
    @DeleteMapping("/api/artist/delete/{artist_id}")
    public void deleteArtistById(@PathVariable("artist_id") int artist_id) {
        Artist artist = artistRepository.findById(artist_id).get();
        artistRepository.delete(artist);
    }
}
