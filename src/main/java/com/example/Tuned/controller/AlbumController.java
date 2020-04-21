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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Service
public class AlbumController {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    ArtistRepository artistRepository;

    @PostMapping("/api/album/{album_id}/song/{song_id}")
    public Album addExistingSongToAlbum(@PathVariable("album_id") int album_id, @PathVariable("song_id") int song_id) {
        if (albumRepository.findById(album_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Album album = albumRepository.findById(album_id).get();
            Song song = songRepository.findById(song_id).get();
            album.addSong(song);
            songRepository.save(song);
            return albumRepository.save(album);
        }
        return null;
    }

    @PostMapping("/api/album/{album_id}/new/song/")
    public Album addNewSongToAlbum(@PathVariable("album_id") int album_id, @RequestBody Song song) {
        if (albumRepository.findById(album_id).isPresent()) {
            Album album = albumRepository.findById(album_id).get();
            album.addSong(song);
            songRepository.save(song);
            return albumRepository.save(album);
        }
        return null;
    }

    @PostMapping("/api/album/{album_id}/song/{song_id}/remove")
    public Album removeSongFromAlbum(@PathVariable("album_id") int album_id, @PathVariable("song_id") int song_id) {
        if (albumRepository.findById(album_id).isPresent()) {
            Album album = albumRepository.findById(album_id).get();
            Song song = songRepository.findById(song_id).get();
            album.removeSong(song);
            songRepository.save(song);
            albumRepository.save(album);
            songRepository.deleteById(song_id); //a song cannot exist without an album;

            return album;
        }
        return null;
    }

    @GetMapping("/api/album/{album_id}")
    public Album findAlbumById(@PathVariable("album_id") int album_id) {
        if (albumRepository.findById(album_id).isPresent())
            return albumRepository.findById(album_id).get();
        else
            return null;
    }

    @GetMapping("api/album/select/all")
    public List<Album> findAllAlbums() {
        List<Album> albums = (List<Album>) albumRepository.findAll();
        return albums;
    }

    @GetMapping("/api/album/{album_id}/artists")
    public Set<Artist> findArtistDetailsForAlbum(@PathVariable("album_id") int album_id) {
        if (albumRepository.findById(album_id).isPresent()) {
            Set<Artist> artists = albumRepository.findArtistDetailsForAlbum(album_id);
            return artists;
        }
        return null;
    }

    //Admin and artist
    @DeleteMapping("/api/album/delete/{album_id}")
    public void deleteAlbumById(@PathVariable("album_id") int album_id) {
        Album album = albumRepository.findById(album_id).get();

        Set<Artist> artists = album.getProducedByArtists();
        if (artists != null) {
            ArtistController artistController = new ArtistController();
            for (Artist a : artists)
                artistController.removeAlbumFromArtist(album_id, a.getUser_id());
        }

        List<Song> songs = album.getSongs();
        if (songs != null)
            for (Song s : songs)
                removeSongFromAlbum(album_id, s.getSong_id());

        albumRepository.delete(album);
    }
}
