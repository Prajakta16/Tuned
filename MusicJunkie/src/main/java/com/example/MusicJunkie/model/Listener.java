package com.example.MusicJunkie.model;

import javax.persistence.*;
import java.util.List;

@Entity(name = "listener")
@Table(name = "listener")
@Inheritance(strategy = InheritanceType.JOINED)
public class Listener extends User{

    private String fav_genre;
    private String disliked_genre;

    @OneToMany(mappedBy = "belongsToListener")
    private List<Playlist> hasPlaylists;

    @OneToMany(mappedBy = "listener")
    private List<Listener_activity> listener_activities;

    public String getFav_genre() {
        return fav_genre;
    }

    public void setFav_genre(String fav_genre) {
        this.fav_genre = fav_genre;
    }

    public String getDisliked_genre() {
        return disliked_genre;
    }

    public void setDisliked_genre(String disliked_genre) {
        this.disliked_genre = disliked_genre;
    }
}
