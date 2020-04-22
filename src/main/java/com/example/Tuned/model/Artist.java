package com.example.Tuned.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "artist")
@Table(name = "artist")
@Inheritance(strategy = InheritanceType.JOINED)
public class Artist extends User {

    private String biography;
    private String image_url;
    private String spotify_url;
    private String spotify_id;
    private Integer popularity;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "album_production",
            joinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id", referencedColumnName = "album_id"))
    //@JsonIgnore
    private Set<Album> producedAlbums;

    public Artist() {
    }

    public Artist(String username, String password, String first_name, String last_name, int phone, String address, String email, String biography, String image_url, String spotify_url, String spotify_id, Integer popularity) {
        super(username, password, first_name, last_name, phone, address, email);
        this.biography = biography;
        this.image_url = image_url;
        this.spotify_url = spotify_url;
        this.spotify_id = spotify_id;
        this.popularity = popularity;
    }

    public void addAlbum(Album album) {
        if (this.getProducedAlbums() == null) {
            Set<Album> albums = new HashSet<>();
            albums.add(album);
            this.setProducedAlbums(albums);
        } else {
            if (!this.getProducedAlbums().contains(album))
                this.getProducedAlbums().add(album);
        }
        if (album.getProducedByArtists() == null) {
            Set<Artist> artists = new HashSet<>();
            artists.add(this);
            album.setProducedByArtists(artists);
        } else {
            if (!album.getProducedByArtists().contains(this))
                album.getProducedByArtists().add(this);
        }
    }

    public void removeAlbumFromArtist(Album album) {
        if (this.getProducedAlbums().contains(album))
            this.producedAlbums.remove(album);
        if (album.getProducedByArtists().contains(this))
            album.getProducedByArtists().remove(this);
    }


    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Set<Album> getProducedAlbums() {
        return producedAlbums;
    }

    public void setProducedAlbums(Set<Album> producedAlbums) {
        this.producedAlbums = producedAlbums;
    }
//public List<Album> getProducedAlbums() {
    //    return producedAlbums;
    //}

    //public void setProducedAlbums(List<Album> producedAlbums) {
    //    this.producedAlbums = producedAlbums;
    //}
}
