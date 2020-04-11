package com.example.MusicJunkie.dao;

import com.example.MusicJunkie.model.Album;
import com.example.MusicJunkie.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AlbumController {

    @Autowired
    AlbumRepository albumRepository;

    public Album createAlbum(Album album){
        return albumRepository.save(album);
    }
}
