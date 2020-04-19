package com.example.Tuned.controller;

import com.example.Tuned.Spotify;
import com.example.Tuned.model.*;
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
        if (!songRepository.findSongByTitle(title).isEmpty())
            songs = songRepository.findSongByTitle(title);
        else {
            String access_token;
            Spotify_token newSpotify_token;
            JSONArray songJsonResponse = new JSONArray();

            LocalTime curr_time = LocalTime.now();
            System.out.println(curr_time);

            Spotify_token spotify_token = new Spotify_token();
            if (spotify_tokenRepository.findById(1).isPresent()) {
                spotify_token = spotify_tokenRepository.findById(1).get(); //there is always going to be just 1 row
                Boolean active = spotify_token.isActive();
                if(active==true){//old token is still active
                    access_token = spotify_token.getToken();
                }
                else{//Creating new token as old one is expired
                    access_token = spotify_token.getActiveToken();
                    newSpotify_token = new Spotify_token(1, access_token, curr_time);
                    spotify_tokenRepository.deleteAll();
                    spotify_tokenRepository.save(newSpotify_token);
                }
            } else { //Creating new token as no token exists
                access_token = spotify_token.getActiveToken();
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
                    String spotify_id = (String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.spotify_id");

                    if(albumRepository.findAlbumBySpotifyId(spotify_id)==null){
                        System.out.println("No album present in db");
                        album.setTitle(album_title);
                        album.setSpotify_url((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.spotify_url"));
                        album.setSpotify_id(spotify_id);
                        album.setImage_url((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.image_url"));
                        String release_date = (String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.release_year");
                        String release_year = release_date.substring(0, 4);
                        album.setRelease_year(release_year);
                    }

                    else{
                        album = albumRepository.findAlbumBySpotifyId(spotify_id);
                    }

                    if(album.getSongs()==null){
                        List<Song> songlist = new ArrayList<>();
                        songlist.add(song);
                        album.setSongs(songlist);
                    }
                    else{
                        if(!album.getSongs().contains(song))
                            album.getSongs().add(song);
                    }
                    if(song.getAlbum()!= album)
                        song.setAlbum(album);

                    Integer count_artists_in_album = JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists.length()");
                    for (int j = 0; j < count_artists_in_album; j++) {
                        Artist artist = new Artist();
                        String username = (String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].name");
                        String artist_spotify_id = (String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].spotify_id");

                        if(artistRepository.findArtistBySpotify_id(artist_spotify_id)== null){
                            artist.setUsername(username);
                            artist.setFirst_name((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].name"));
                            artist.setLast_name((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].name"));
                            artist.setPassword("artistpass");
                            artist.setEmail(username.substring(1, 3) + "@tuned.com");
                            artist.setPhone((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
                            artist.setSpotify_id(artist_spotify_id);
                            artist.setSpotify_url((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].spotify_url"));
                        }
                        else {
                            artist = artistRepository.findArtistBySpotify_id(artist_spotify_id);
                        }

                        if(artist.getProducedAlbums()==null){
                            List<Album> albums = new ArrayList<>();
                            albums.add(album);
                            artist.setProducedAlbums(albums);
                        }
                        else{
                            if(!artist.getProducedAlbums().contains(album))
                                artist.getProducedAlbums().add(album);
                        }
                        if(album.getProducedByArtists()==null){
                            List<Artist> artists = new ArrayList<>();
                            artists.add(artist);
                            album.setProducedByArtists(artists);
                        }
                        else{
                            if(!album.getProducedByArtists().contains(artist))
                                album.getProducedByArtists().add(artist);
                        }
                        artistRepository.save(artist);
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