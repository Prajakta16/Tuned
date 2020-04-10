package com.example.MusicJunkie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.groups.Default;
import java.util.List;

@Entity(name = "artist")
@Table(name="artist")
@Inheritance(strategy = InheritanceType.JOINED)
public class Artist extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int artist_id;

    @Column(columnDefinition = "varchar(20) DEFAULT 'singer'")
    private String artist_type;

    private String image_url;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="song_production",
            joinColumns= @JoinColumn(name="artist_id", referencedColumnName="artist_id"),
            inverseJoinColumns= @JoinColumn(name= "song_id", referencedColumnName="song_id"))
    @JsonIgnore
    private List<Song> producedSongs;

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

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
}
