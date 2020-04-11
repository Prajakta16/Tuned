package com.example.MusicJunkie.controller;

import com.example.MusicJunkie.model.Song;
import com.example.MusicJunkie.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Service
public class SongController {

    @Autowired
    SongRepository songRepository;

    @PostMapping("/api/song")
    public Song createSong(@RequestBody Song song){
        return songRepository.save(song);
    }
}
