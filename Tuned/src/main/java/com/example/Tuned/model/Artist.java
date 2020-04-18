package com.example.Tuned.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity(name = "artist")
@Table(name="artist")
@Inheritance(strategy = InheritanceType.JOINED)
public class Artist extends User {

    @Column(columnDefinition = "varchar(20) DEFAULT 'singer'")
    private String artist_type;

    private String image_url;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "song_production",
            joinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id", referencedColumnName = "song_id"))
    @JsonIgnore
    private List<Song> producedSongs;


    public String getArtist_type() {
        return artist_type;
    }

    public void setArtist_type(String artist_type) {
        this.artist_type = artist_type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List<Song> getProducedSongs() {
        return producedSongs;
    }

    public void setProducedSongs(List<Song> producedSongs) {
        this.producedSongs = producedSongs;
    }

    public void addSongs(Song song) {
        this.producedSongs.add(song);
        if (song.getProducedByArtists() != this) {
            song.setProducedByArtists((List<Artist>) this);
        }
    }
}
