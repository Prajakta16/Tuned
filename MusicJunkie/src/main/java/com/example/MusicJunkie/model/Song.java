package com.example.MusicJunkie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity(name="song")
@Table(name="song")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int song_id;

    private String title;
    private String genre;
    private String description;
    private String image_url;
    private int duration;
    private int year_released;
    private int popularity;
    private int energy;

    @OneToMany(mappedBy = "song")
    private List<Listener_activity> activities;

    @ManyToMany(mappedBy = "producedSongs", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Artist> producedByArtists;

    @ManyToMany(mappedBy = "songs", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Playlist> playlists;

    @ManyToMany(mappedBy = "songs", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Album> albums;

    public List<Listener_activity> getActivities() {
        return activities;
    }

    public Song(String title, String genre, String description, int duration, int year_released, int popularity, int energy) {
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.duration = duration;
        this.year_released = year_released;
        this.popularity = popularity;
        this.energy = energy;
    }

    public Song() {
    }

    public void setActivities(List<Listener_activity> activities) {
        this.activities = activities;
    }

    public List<Artist> getProducedByArtists() {
        return producedByArtists;
    }

    public void setProducedByArtists(List<Artist> producedByArtists) {
        this.producedByArtists = producedByArtists;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getYear_released() {
        return year_released;
    }

    public void setYear_released(int year_released) {
        this.year_released = year_released;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
