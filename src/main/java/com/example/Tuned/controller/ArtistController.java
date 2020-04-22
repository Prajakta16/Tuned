package com.example.Tuned.controller;

import com.example.Tuned.model.*;
import com.example.Tuned.repository.AlbumRepository;
import com.example.Tuned.repository.ArtistRepository;
import com.example.Tuned.repository.SongRepository;
import com.example.Tuned.repository.UserRepository;
import org.json.simple.JSONObject;
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
    UserRepository userRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    AlbumRepository albumRepository;

    @PostMapping("/api/artist/new")
    JSONObject createArtist(@RequestBody Artist artist){
        artist.setUser_type("artist");
        List<Artist> artist1 = artistRepository.findArtistByUsername(artist.getUsername());
        JSONObject jsonObject = new JSONObject();
        if(artist1!=null && !artist1.isEmpty()){
            jsonObject.put("error", "Username "+ artist.getUsername() +" already exists.");
            return jsonObject;
        }

        try {
            Artist newL = artistRepository.save(artist);
            jsonObject.put("success", "success");
            jsonObject.put("user_id", newL.getUser_id());
            return jsonObject;
        }catch (Exception e){
            jsonObject.put("error", "Some error occurred");
            return jsonObject;
        }

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
        if (artistRepository.findById(artist_id).isPresent()) {
            return artistRepository.findById(artist_id).get();
        }
        return null;
    }

    @GetMapping("/api/artist/{artist_id}/albums/all")
    public Set<Album> findAlbumsOfArtist(@PathVariable("artist_id") int artist_id) {
        if (artistRepository.findById(artist_id).isPresent()) {
            Artist artist = artistRepository.findById(artist_id).get();
            return artist.getProducedAlbums();
        }
        return null;
    }

    @DeleteMapping("/api/artist/{artist_id}/delete")
    public JSONObject deleteArtist(@PathVariable("artist_id") int artist_id){
        if (userRepository.findById(artist_id).isPresent()) {
            userRepository.deleteById(artist_id);
            JSONObject jsonObject = new JSONObject();

            if(!userRepository.findById(artist_id).isPresent())
                jsonObject.put("Success", "true");
            else
                jsonObject.put("Success", "false");
            return jsonObject;
        }
        return null;
    }

}
