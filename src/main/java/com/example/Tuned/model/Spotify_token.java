package com.example.Tuned.model;


import java.sql.Timestamp;
import java.time.LocalTime;
import javax.persistence.*;

@Entity(name = "spotify_token")
@Table(name = "spotify_token")
public class Spotify_token {

    @Id
    private int token_id;
    String token;
    LocalTime time;

    public Spotify_token(int token_id,String token, LocalTime time) {
        this.token_id=token_id;
        this.time=time;
        this.token = token;
    }

    public Spotify_token() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
