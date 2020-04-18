package com.example.Tuned.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name="song")
@Table(name="song")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int song_id;

    private String title;
    private int duration;
    private int popularity;
    private String preview_url;
    private String spotify_url;
    private String spotify_id;

    @OneToMany(mappedBy = "song", fetch = FetchType.EAGER)
    private Set<Listener_activity> activities;

    @ManyToMany(mappedBy = "songs")
    @JsonIgnore
    private Set<Playlist> playlists = new HashSet<Playlist>();

    @ManyToMany(mappedBy = "songs", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Album> albums;

    public Song(String title, int duration, int popularity, String preview_url, String spotify_url, String spotify_id) {
        this.title = title;
        this.duration = duration;
        this.popularity = popularity;
        this.preview_url = preview_url;
        this.spotify_url = spotify_url;
        this.spotify_id = spotify_id;
    }

    public Song() {
    }

    public void setActivities(Listener_activity listener_activity) {
        this.getActivities().add(listener_activity);
        if(listener_activity.getSong()!= this)
            listener_activity.setSong(this);
    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public String getSpotify_url() {
        return spotify_url;
    }

    public void setSpotify_url(String spotify_url) {
        this.spotify_url = spotify_url;
    }

    public String getSpotify_id() {
        return spotify_id;
    }

    public void setSpotify_id(String spotify_id) {
        this.spotify_id = spotify_id;
    }

    public Set<Listener_activity> getActivities() {
        return activities;
    }

//    public void setActivities(Set<Listener_activity> activities) {
//        this.activities = activities;
//    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}
