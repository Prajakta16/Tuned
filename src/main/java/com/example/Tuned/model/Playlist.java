package com.example.Tuned.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity(name = "playlist")
@Table(name = "playlist")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playlist_id;

    private String title;

    @Column(columnDefinition = "int(11) DEFAULT '0'")
    private int visits;

    @ManyToOne
    @JsonIgnore
    private Listener listener;

    //cascade => To prevent deletion of child
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name = "playlist_detail",
            joinColumns = @JoinColumn(name = "playlist_id", referencedColumnName = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id", referencedColumnName = "song_id")
    )
    //@JsonIgnore
    private Set<Song> songs;
    //private List<Song> songs;

    public Playlist(String title, String description) {
        this.title=title;
    }

    public Playlist() {
    }

    public void setListener(Listener listener) {
        this.listener = listener;
        if(!listener.getPlaylists().contains(this))
            listener.getPlaylists().add(this);
    }

    public void addSong(Song song){
        this.songs.add(song);
        if(!song.getPlaylists().contains(this))
            song.getPlaylists().add(this);
    }

    public void removeSong(Song song){
        this.songs.remove(song);
        if(song.getPlaylists().contains(this))
            song.getPlaylists().remove(this);
    }

    public int getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(int playlist_id) {
        this.playlist_id = playlist_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public Listener getListener() {
        return listener;
    }

    //public List<Song> getSongs() {
    //    return songs;
    //}

    //public void setSongs(List<Song> songs) {
    //    this.songs = songs;
    //}
    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }
}
