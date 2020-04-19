package com.example.Tuned.controller;

import com.example.Tuned.Spotify;
import com.example.Tuned.model.Album;
import com.example.Tuned.model.Artist;
import com.example.Tuned.model.Song;
import com.example.Tuned.model.Spotify_token;
import com.example.Tuned.repository.AlbumRepository;
import com.example.Tuned.repository.ArtistRepository;
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

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @GetMapping("/api/song/search/{title}")
    public List<Song> findSongByTitle(@PathVariable("title") String title) {

        List<Song> songs = new ArrayList<>();
        if (songRepository.findSongByTitle(title) != null)
            songs = songRepository.findSongByTitle(title);

        else {
            String access_token;
            Spotify_token newSpotify_token;
            JSONArray songJsonResponse = new JSONArray();

            LocalTime curr_time = LocalTime.now();
            System.out.println(curr_time);

            if (spotify_tokenRepository.findById(1).isPresent()) {
                Spotify_token spotify_token = spotify_tokenRepository.findById(1).get(); //there is always going to be just 1 row

                LocalTime last_time = spotify_token.getTime();
                System.out.println(last_time);
                System.out.println(ChronoUnit.MINUTES.between(last_time, curr_time));

                if (ChronoUnit.MINUTES.between(last_time, curr_time) <= 60 && ChronoUnit.MINUTES.between(last_time, curr_time) >= -60) {
                    access_token = spotify_token.getToken();
                    System.out.println("Token still active");
                } else {
                    System.out.println("Creating new token as old one is expired");
                    access_token = spotify.getNewAccessCode();
                    newSpotify_token = new Spotify_token(1, access_token, curr_time);
                    spotify_tokenRepository.deleteAll();
                    spotify_tokenRepository.save(newSpotify_token);
                }
                System.out.println(access_token);
            } else {
                System.out.println("Creating new token as no token exists");
                access_token = spotify.getNewAccessCode();
                newSpotify_token = new Spotify_token(1, access_token, curr_time);
                spotify_tokenRepository.save(newSpotify_token);
            }

            try {
                songJsonResponse = spotify.searchSong(access_token, title);

                Integer count_songs = JsonPath.read(songJsonResponse, "$.length()");
                for (int i = 0; i < count_songs; i++) {
                    Song song = new Song();
                    song.setTitle((String) JsonPath.read(songJsonResponse, "$.[" + i + "].title"));
                    song.setDuration((int) JsonPath.read(songJsonResponse, "$.[" + i + "].duration"));
                    song.setSpotify_id((String) JsonPath.read(songJsonResponse, "$.[" + i + "].spotify_id"));
                    song.setSpotify_url((String) JsonPath.read(songJsonResponse, "$.[" + i + "].spotify_url"));
                    song.setPreview_url((String) JsonPath.read(songJsonResponse, "$.[" + i + "].preview_url"));
                    song.setPopularity((int) JsonPath.read(songJsonResponse, "$.[" + i + "].popularity"));

                    Album album = new Album();
                    String album_title = (String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.title");

                    if(albumRepository.findAlbumByTitle(album_title)==null){
                        album.setTitle(album_title);
                        album.setSpotify_url((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.spotify_url"));
                        album.setSpotify_id((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.spotify_id"));
                        album.setImage_url((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.image_url"));
                        String release_date = (String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.release_year");
                        String release_year = release_date.substring(0, 4);
                        album.setRelease_year(release_year);
                    }
                    else{
                        album = albumRepository.findAlbumByTitle(album_title);
                    }

                    if(!album.getSongs().contains(song))
                        album.getSongs().add(song);
                    song.setAlbum(album);

                    Integer count_artists_in_album = JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists.length()");
                    for (int j = 0; j < count_artists_in_album; j++) {

                        Artist artist = new Artist();
                        String username = (String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].name");

                        if(artistRepository.findArtistByUsername(username)== null){
                            artist.setUsername(username);
                            artist.setFirst_name((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].name"));
                            artist.setLast_name((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].name"));
                            artist.setPassword("artistpass");
                            artist.setEmail(username.substring(1, 3) + "@tuned.com");
                            artist.setPhone((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
                            artist.setSpotify_id((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].spotify_id"));
                            artist.setSpotify_url((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].spotify_url"));
                        }
                        else {
                            artist = artistRepository.findArtistByUsername(username);
                        }

                        if(!artist.getProducedAlbums().contains(album))
                            artist.getProducedAlbums().add(album);

                        artistRepository.save(artist);
                        if(!album.getProducedByArtists().contains(artist))
                            album.getProducedByArtists().add(artist);
                    }
                    albumRepository.save(album);
                    songRepository.save(song);
                }

                return songJsonResponse;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return songs;
    }
}