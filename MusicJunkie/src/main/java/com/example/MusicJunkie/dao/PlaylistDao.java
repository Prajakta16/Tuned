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
import java.util.Optional;

@Service
public class PlaylistDao {

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    ListenerRepository listenerRepository;

    @Autowired
    SongRepository songRepository;

    public Playlist createPlaylistForListener(Playlist playlist, Listener listener) {
        if (listenerRepository.findById(listener.getUser_id()).isPresent()) {
            playlist.setListener(listener);
            if (listener.getPlaylists() == null) {
                List<Playlist> playlistsForListener = new ArrayList<>();
                playlistsForListener.add(playlist);
                listener.setPlaylists(playlistsForListener);
            } else
                listener.getPlaylists().add(playlist);
            listenerRepository.save(listener);
            return playlistRepository.save(playlist);
        }
        return null;
    }

    public void deletePlaylistById(int playlist_id) {
        Playlist playlist = playlistRepository.findById(playlist_id).get();
        playlistRepository.delete(playlist);
    }

    public Playlist findPlaylistById(int playlist_id) {
        if (playlistRepository.findById(playlist_id).isPresent())
            return playlistRepository.findById(playlist_id).get();
        else
            return null;
    }

    public List<Playlist> findAllPlaylistsForListener(int listener_id) {
        if (listenerRepository.findById(listener_id).isPresent()) {
            Listener listener = listenerRepository.findById(listener_id).get();
            return listener.getPlaylists();
        }
        return null;
    }

    public List<Playlist> findAllPlaylists() {
        return (List<Playlist>) playlistRepository.findAll();
    }

    public void addSongToPlaylist(int song_id, int playlist_id) {
        if (playlistRepository.findById(playlist_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Song song = songRepository.findById(song_id).get();
            Playlist playlist = playlistRepository.findById(playlist_id).get();
            playlist.addSong(song);
            songRepository.save(song);
            playlistRepository.save(playlist);
        }
    }

    public void removeSongFromPlaylist(int song_id, int playlist_id) {
        if (playlistRepository.findById(playlist_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Song song = songRepository.findById(song_id).get();
            Playlist playlist = playlistRepository.findById(playlist_id).get();
            playlist.removeSong(song);
        }
    }

}
