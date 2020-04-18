package com.example.Tuned.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name="album")
@Table(name="album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int album_id;

    private String title;
    private String description;
    private String movie;
    private String year;
    private Date year_released;
    private String image_url;
    private int visits;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "album_detail",
            joinColumns = @JoinColumn(name = "album_id", referencedColumnName = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id", referencedColumnName = "song_id")
    )
    @JsonIgnore
    //private List<Song> songs;
    private Set<Song> songs = new HashSet<Song>();


    public Album(String title) {
        this.title=title;
    }

    public Album(){

    }

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getYear_released() {
        return year_released;
    }

    public void setYear_released(Date year_released) {
        this.year_released = year_released;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }


    public void addSong(Song song){
        this.songs.add(song);
        if(!song.getAlbums().contains(this))
            song.getAlbums().add(this);
    }

    public void removeSong(Song song){
        this.songs.remove(song);
        if(song.getAlbums().contains(this))
            song.getAlbums().remove(this);
    }
}
