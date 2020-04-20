package com.example.Tuned.controller;

import com.example.Tuned.model.Album;
import com.example.Tuned.model.Playlist;
import com.example.Tuned.model.Song;
import com.example.Tuned.repository.AlbumRepository;
import com.example.Tuned.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Service
public class AlbumController {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    SongRepository songRepository;

    @PostMapping("/api/album/new")
    Album createAlbum(@RequestBody Album album){
        return albumRepository.save(album);
    }


    @GetMapping("/api/album/{album_id}")
    public Album findAlbumById(@PathVariable("album_id") int album_id) {
        if (albumRepository.findById(album_id).isPresent())
            return albumRepository.findById(album_id).get();
        else
            return null;
    }

    @RequestMapping("/api/album/insert/{title}")
    public Album insertAlbum(@PathVariable("title") String title) {
        Album alb = new Album();
        alb.setTitle(title);
        albumRepository.save(alb);
        return alb;
    }

    @GetMapping("/api/album/name/{title}")
    public Album findAlbumByTitle(@PathVariable("title") String title) {
        return albumRepository.findAlbumByTitle(title);
    }


    @GetMapping("api/album/select/all")
    public List<Album> findAllAlbums() {
        List<Album> albums = (List<Album>) albumRepository.findAll();
        return albums;
    }

    @PostMapping("/api/album/{album_id}/song/{song_id}")
    public Album addSongToAlbum(@PathVariable("album_id") int album_id, @PathVariable("song_id") int song_id) {
        if (albumRepository.findById(album_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Album album = albumRepository.findById(album_id).get();
            Song song = songRepository.findById(song_id).get();
            album.addSong(song);
            songRepository.save(song);
            return albumRepository.save(album);
        }
        return null;
    }

    @DeleteMapping("/api/album/delete/{album_id}")
    public void deleteAlbumById(@PathVariable("album_id") int album_id) {
        Album album = albumRepository.findById(album_id).get();
        albumRepository.delete(album);
    }
}
