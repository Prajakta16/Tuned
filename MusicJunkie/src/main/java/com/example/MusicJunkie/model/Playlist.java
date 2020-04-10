package com.example.MusicJunkie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity(name = "playlist")
@Table(name = "playlist")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playlist_id;

    private String title;
    private String description;

    @Column(columnDefinition = "int(11) DEFAULT '0'")
    private int visits;

    @ManyToOne
    @JsonIgnore
    private Listener belongsToListener;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "playlist_detail",
            joinColumns = @JoinColumn(name = "playlist_id", referencedColumnName = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id", referencedColumnName = "song_id")
    )
    @JsonIgnore
    private List<Song> songs;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
}
