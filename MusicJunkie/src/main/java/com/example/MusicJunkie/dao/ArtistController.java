package com.example.MusicJunkie.dao;

import com.example.MusicJunkie.model.Artist;
import com.example.MusicJunkie.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ArtistController {

    @Autowired
    ArtistRepository artistRepository;

    public Artist createArtist(Artist artist){
        return artistRepository.save(artist);
    }
}
