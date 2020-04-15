package com.example.MusicJunkie.dao;

import com.example.MusicJunkie.model.Listener;
import com.example.MusicJunkie.model.Playlist;
import com.example.MusicJunkie.model.Song;
import com.example.MusicJunkie.repository.ListenerRepository;
import com.example.MusicJunkie.repository.PlaylistRepository;
import com.example.MusicJunkie.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistDao {

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    ListenerRepository listenerRepository;

    @Autowired
    SongRepository songRepository;

    public Playlist  createPlaylist(Playlist playlist){
        return playlistRepository.save(playlist);
    }

    public void deletePlaylist(Playlist playlist){
         playlistRepository.delete(playlist);
    }

    public List<Playlist> findAllPlaylistsForListener(int listener_id){
        if(listenerRepository.findById(listener_id).isPresent()){
            Listener listener = listenerRepository.findById(listener_id).get();
            return listener.getPlaylists();
        }
        return null;
    }

    public List<Playlist> findAllPlaylists(){
        return (List<Playlist>) playlistRepository.findAll();
    }

    public void addSongToPlaylist(int song_id, int playlist_id){
        if(playlistRepository.findById(playlist_id).isPresent() && songRepository.findById(song_id).isPresent()){
            Song song = songRepository.findById(song_id).get();
            Playlist playlist = playlistRepository.findById(playlist_id).get();
            if(playlist.getSongs() == null){
                List<Song> songsInPlaylist = new ArrayList<>();
                songsInPlaylist.add(song);
                playlist.setSongs(songsInPlaylist);
            }
            else{
                playlist.getSongs().add(song);
            }
            if(song.getPlaylists() == null){
                List<Playlist>  playlistsForSong = new ArrayList<>();
                playlistsForSong.add(playlist);
                song.setPlaylists(playlistsForSong);
            }
            else{
                song.getPlaylists().add(playlist);
            }
        }

    }

}
