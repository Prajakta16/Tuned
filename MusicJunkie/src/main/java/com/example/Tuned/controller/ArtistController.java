package com.example.Tuned.controller;

import com.example.Tuned.model.Artist;
import com.example.Tuned.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Service
public class ArtistController {

    @Autowired
    ArtistRepository artistRepository;

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
}
