package com.example.Tuned.controller;

import com.example.Tuned.model.Album;
import com.example.Tuned.model.Artist;
import com.example.Tuned.model.Spotify_token;
import com.example.Tuned.repository.AlbumRepository;
import com.example.Tuned.repository.ArtistRepository;
import com.example.Tuned.repository.SongRepository;
import com.example.Tuned.repository.Spotify_tokenRepository;
import com.example.Tuned.spotifyUtility.Spotify;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@Service
public class SpotifySaveController {

    @Autowired
    Spotify_tokenRepository spotify_tokenRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    Spotify spotify = new Spotify();

    public void saveArtistInDb(String name) {

        String access_token = fetchToken();
        JSONArray artistJsonResponse = new JSONArray();
        try {
            Artist artist = new Artist();
            artistJsonResponse = spotify.searchArtist(access_token, name);
            System.out.println(artistJsonResponse);

            int count_artists= JsonPath.read(artistJsonResponse, "$.length()");

            for(int i=0; i< count_artists; i++) {
                String username = JsonPath.read(artistJsonResponse, "$.[" + i + "].name");
                artist.setUser_type("artist");
                artist.setAddress("address of "+username);
                artist.setUsername(username);
                artist.setFirst_name(username);
                artist.setLast_name(username);
                artist.setPassword("artistpass");
                artist.setEmail(username.substring(0, 3) + "@tuned.com");
                artist.setPhone((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
                artist.setSpotify_id(JsonPath.read(artistJsonResponse, "$.[" + i + "].spotify_url"));
                artist.setSpotify_url(JsonPath.read(artistJsonResponse, "$.[" + i + "].spotify_id"));
                artist.setImage_url(JsonPath.read(artistJsonResponse, "$.[" + i + "].image_url"));
                artist.setPopularity(JsonPath.read(artistJsonResponse, "$.[" + i + "].popularity"));
                //set followers
                //set genres
                //set href

                int count_albums = JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums.length()");
                for (int j = 0; j < count_albums; j++) {
                    Album album = new Album();
                    album.setTitle(JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums[" + j + "].title"));
                    album.setSpotify_url(JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums[" + j + "].spotify_url"));
                    album.setSpotify_id(JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums[" + j + "].spotify_id"));
                    album.setImage_url(JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums[" + j + "].image_url"));
                    album.setAlbum_type(JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums[" + j + "].album_type"));
                    album.setPopularity(JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums[" + j + "].album_details.popularity"));
                    String release_date = JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums[" + j + "].release_year");
                    String release_year = release_date.substring(0, 4);
                    album.setRelease_year(release_year);

                    artist.addAlbum(album);
                    albumRepository.save(album);
                    artistRepository.save(artist);
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public String fetchToken() {
        Spotify_token newSpotify_token;
        String access_token;
        LocalTime curr_time = LocalTime.now();
        System.out.println(curr_time);

        Spotify_token spotify_token = new Spotify_token();
        if (spotify_tokenRepository.findById(1).isPresent()) {
            spotify_token = spotify_tokenRepository.findById(1).get(); //there is always going to be just 1 row
            Boolean active = spotify_token.isActive();
            if (active == true) {//old token is still active
                access_token = spotify_token.getToken();
            } else {//Creating new token as old one is expired
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
        return access_token;
    }
}
