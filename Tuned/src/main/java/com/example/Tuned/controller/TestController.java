package com.example.Tuned.controller;

import com.example.Tuned.Spotify;
import com.example.Tuned.model.Song;
import com.example.Tuned.model.Spotify_token;
import com.example.Tuned.repository.SongRepository;
import com.example.Tuned.repository.Spotify_tokenRepository;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import com.jayway.jsonpath.JsonPath;

import java.net.URISyntaxException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@Service
public class TestController {

    Spotify spotify = new Spotify();

    @Autowired
    Spotify_tokenRepository spotify_tokenRepository;

    @Autowired
    SongRepository songRepository;

    @GetMapping("/api/song/search/{title}")
    public List<Song> findSongByTitle(@PathVariable("title") String title) {

        List<Song> songs = new ArrayList<>();
        String access_token;
        Spotify_token newSpotify_token;
        JSONArray songJsonResponse = new JSONArray();

        LocalTime curr_time = LocalTime.now();
        System.out.println(curr_time);

        if(spotify_tokenRepository.findById(1).isPresent()){
            Spotify_token spotify_token = spotify_tokenRepository.findById(1).get(); //there is always going to be just 1 row

            LocalTime last_time = spotify_token.getTime();
            System.out.println(last_time);
            System.out.println(ChronoUnit.MINUTES.between(last_time, curr_time));

            if(ChronoUnit.MINUTES.between(last_time, curr_time) <= 60 && ChronoUnit.MINUTES.between(last_time, curr_time) >= -60 ){
                access_token = spotify_token.getToken();
                System.out.println("Token still active");
            }
            else{
                System.out.println("Creating new token as old one is expired");
                access_token = spotify.getNewAccessCode();
                newSpotify_token = new Spotify_token(1,access_token,curr_time);
                spotify_tokenRepository.deleteAll();
                spotify_tokenRepository.save(newSpotify_token);
            }
            System.out.println(access_token);
        }
        else{
            System.out.println("Creating new token as no token exists");
            access_token = spotify.getNewAccessCode();
            newSpotify_token = new Spotify_token(1,access_token,curr_time);
            spotify_tokenRepository.save(newSpotify_token);
        }

        try{
            songJsonResponse = spotify.searchSong(access_token,title);

            Integer count_songs = JsonPath.read(songJsonResponse, "$.length()");
            for(int i=0; i<count_songs;i++){
                Song song = new Song();
                song.setTitle((String) JsonPath.read(songJsonResponse, "$.["+i+"].title"));

                Integer count_artists_in_song = JsonPath.read(songJsonResponse, "$.[" + i + "].artists.length()");
                for (int j = 0; j < count_artists_in_song; j++) {
                    String artist_name = (String) JsonPath.read(songJsonResponse, "$.[" + i + "].artists[" + j + "].artist_name");
                    String artist_spotify_id = (String) JsonPath.read(songJsonResponse, "$.[" + i + "].artists[" + j + "].artist_id");
                    System.out.println(artist_name);
                    System.out.println(artist_spotify_id);
                }

                songRepository.save(song);
            }

            return songJsonResponse;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}