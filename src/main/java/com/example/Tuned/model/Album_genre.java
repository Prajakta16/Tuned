package com.example.Tuned.model;

import javax.persistence.*;

@Entity(name="album_genre")
@Table(name="album_genre")
public class Album_genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int album_genre_id;
    private String genre;

    @ManyToOne
    private Album album;

    public Album_genre(String genre){
        this.genre=genre;
    }

    public Album_genre(){
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
