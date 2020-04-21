package com.example.Tuned.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity(name = "album")
@Table(name = "album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int album_id;

    private String title;
    private int visits;
    private Integer popularity;
    private String album_type;
    private String release_year;
    private String spotify_url;
    private String spotify_id;
    private String image_url;

    @OneToMany(mappedBy = "album")
    private List<Album_genre> album_genres;

    //cascade => to cascade delete
    @OneToMany(mappedBy = "album", cascade = CascadeType.REMOVE)
    //@JsonIgnore
    private List<Song> songs;

    @ManyToMany(mappedBy = "producedAlbums")
    @JsonIgnore
    private Set<Artist> producedByArtists;

    public Album() {
    }

    public Album(String title, Integer popularity, String album_type, String release_year, String spotify_url, String spotify_id, String image_url) {
        this.title = title;
        this.popularity = popularity;
        this.album_type = album_type;
        this.release_year = release_year;
        this.spotify_url = spotify_url;
        this.spotify_id = spotify_id;
        this.image_url = image_url;
    }

    public void addGenre(Album_genre album_genre) {
        this.getAlbum_genres().add(album_genre);
        if (album_genre.getAlbum() != this)
            album_genre.setAlbum(this);
    }

    public void addSong(Song song) {
        if (this.getSongs() == null) {
            List<Song> song_list = new ArrayList<>();
            song_list.add(song);
            this.setSongs(song_list);
        } else {
            if (!this.getSongs().contains(song))
                this.getSongs().add(song);
        }
        if (song.getAlbum() != this)
            song.setAlbum(this);

    }

    public void removeSong(Song song) {
        this.getSongs().remove(song);
        if (song.getAlbum() == this)
            song.setAlbum(null);
    }

//    public void addArtist(Artist artist){
//        if(this.getProducedByArtists() == null){
//            Set<Artist> artists = new HashSet<>();
//            artists.add(artist);
//            this.setProducedByArtists(artists);
//        }
//        else{
//            if(!this.getProducedByArtists().contains(artist))
//                this.getProducedByArtists().add(artist);
//        }
//        if(artist.getProducedAlbums()==null){
//            Set<Album> albums = new HashSet<>();
//            albums.add(this);
//            artist.setProducedAlbums(albums);
//        }
//        else{
//            if(!artist.getProducedAlbums().contains(this))
//                artist.getProducedAlbums().add(this);
//        }
//    }

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

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public String getAlbum_type() {
        return album_type;
    }

    public void setAlbum_type(String album_type) {
        this.album_type = album_type;
    }

    public String getRelease_year() {
        return release_year;
    }

    public void setRelease_year(String release_year) {
        this.release_year = release_year;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    //public List<Artist> getProducedByArtists() {
    //    return producedByArtists;
    //}

    //public void setProducedByArtists(List<Artist> producedByArtists) {
    //    this.producedByArtists = producedByArtists;
    //}

    public Set<Artist> getProducedByArtists() {
        return producedByArtists;
    }

    public void setProducedByArtists(Set<Artist> producedByArtists) {
        this.producedByArtists = producedByArtists;
    }

    public List<Album_genre> getAlbum_genres() {
        return album_genres;
    }

    public void setAlbum_genres(List<Album_genre> album_genres) {
        this.album_genres = album_genres;
    }
}
