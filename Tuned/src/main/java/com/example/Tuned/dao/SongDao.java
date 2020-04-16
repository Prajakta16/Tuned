package com.example.Tuned.dao;

import com.example.Tuned.model.Playlist;
import com.example.Tuned.model.Song;
import com.example.Tuned.repository.PlaylistRepository;
import com.example.Tuned.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SongDao {

    @Autowired
    SongRepository songRepository;

    @Autowired
    PlaylistRepository playlistRepository;

    public Song createSong(Song song){
        return songRepository.save(song);
    }

    public void deleteSongById(int song_id){
        Song song = songRepository.findById(song_id).get();
        Set<Playlist> playlists = song.getPlaylists();
        for(Playlist p: playlists) {
            p.removeSong(song);
            playlistRepository.save(p);
        }
         songRepository.delete(song);
    }

    public List<Song> findAllSongs(){
        return (List<Song>) songRepository.findAll();
    }

    public List<Song> findSongsByTitle(String title){
        return (List<Song>) songRepository.findSongByTitle(title);
    }

    public List<Song> findSongsByGenre(String genre){
        return (List<Song>) songRepository.findSongByGenre(genre);
    }

    public Song findSongById(int id){
        if(songRepository.findById(id).isPresent())
            return songRepository.findById(id).get();
        else
            return null;
    }
}
