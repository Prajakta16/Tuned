package com.example.Tuned.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity(name = "listener")
@Table(name = "listener")
@Inheritance(strategy = InheritanceType.JOINED)
public class Listener extends User{

    private String fav_genre;
    private String disliked_genre;

    @OneToMany(mappedBy = "listener", cascade = CascadeType.ALL)
    private List<Playlist> playlists;

    @OneToMany(mappedBy = "listener", cascade = CascadeType.ALL)
    private List<Listener_activity> listener_activities;

    public Listener(){
        super();
    }

    public Listener(String username, String password, String first_name, String last_name, int phone, String address, String email, String fav_genre, String disliked_genre) {
        super(username, password, first_name, last_name, phone, address, email);
        this.fav_genre=fav_genre;
        this.disliked_genre=disliked_genre;
    }

    public void addPlaylist(Playlist playlist){
        this.getPlaylists().add(playlist);
        if(playlist.getListener()!=this)
            playlist.setListener(this);
    }


    public void removePlaylist(Playlist playlist){
        System.out.println("aaaa");
        this.getPlaylists().remove(playlist);
        System.out.println("bbbbb");
    }

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

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public List<Listener_activity> getListener_activities() {
        return listener_activities;
    }

    public void setListener_activities(List<Listener_activity> listener_activities) {
        this.listener_activities = listener_activities;
    }

//    public void setListener_activities(List<Listener_activity> listener_activities) {
//        this.listener_activities = (Set<Listener_activity>) listener_activities;
//    }
//
//    public Set<Listener_activity> getSongActivity() {
//        return listener_activities;
//    }
}
