package com.example.MusicJunkie.controller;

import com.example.MusicJunkie.model.Album;
import com.example.MusicJunkie.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Service
public class AlbumController {

    @Autowired
    AlbumRepository albumRepository;

    @PostMapping("/api/album")
    public Album createAlbum(@RequestBody Album album){
        return albumRepository.save(album);
    }

//    @RequestMapping("api/album/select")
//    public Album findAlbumByTitle(/*@PathVariable("title") String title*/){
//        return albumRepository.findAlbumByTitle("PDAlbum");
//    }
//
//    @GetMapping("api/album/select/id/{album_id}")
//    public Optional<Album> findAlbumById(@PathVariable("album_id") int album_id){
//        return albumRepository.findById(album_id);
//    }

    @RequestMapping("/api/album/insert/{title}")
    public Album insertAlbum(@PathVariable("title") String title) {
        Album alb = new Album(title);
        albumRepository.save(alb);
        return alb;
    }

    @GetMapping("api/album/select/all")
    public List<Album> findAllAlbums() {
        List<Album> albums = (List<Album>) albumRepository.findAll();
        return albums;
    }
//
//    @RequestMapping("/api/hello/string")
//    public String sayHello() {
//        return "Hello Prajakta string!";
//    }
}
