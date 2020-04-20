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

@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    public List<Song> searchSongByTitle(@PathVariable("title") String title) {

        List<Song> songs = new ArrayList<>();
        if (!songRepository.findSongByTitle(title).isEmpty())
            songs = songRepository.findSongByTitle(title);
        else {
            String access_token = fetchToken();
            JSONArray songJsonResponse = new JSONArray();

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

                    if (albumRepository.findAlbumBySpotifyId(spotify_id) == null) {
                        System.out.println("No album present in db");
                        album.setTitle(album_title);
                        album.setSpotify_url((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.spotify_url"));
                        album.setSpotify_id(spotify_id);
                        album.setImage_url((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.image_url"));
                        album.setAlbum_type((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.album_details.album_type"));
                        album.setPopularity((Integer) JsonPath.read(songJsonResponse, "$.[" + i + "].album.album_details.popularity"));
                        //add genres to album/////////////////////////
                        String release_date = (String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.release_year");
                        String release_year = release_date.substring(0, 4);
                        album.setRelease_year(release_year);
                    } else {
                        album = albumRepository.findAlbumBySpotifyId(spotify_id);
                    }

                    if (album.getSongs() == null) {
                        List<Song> songlist = new ArrayList<>();
                        songlist.add(song);
                        album.setSongs(songlist);
                    } else {
                        if (!album.getSongs().contains(song))
                            album.getSongs().add(song);
                    }
                    if (song.getAlbum() != album)
                        song.setAlbum(album);

                    Integer count_artists_in_album = JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists.length()");
                    for (int j = 0; j < count_artists_in_album; j++) {
                        Artist artist = new Artist();
                        String username = (String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].name");
                        String artist_spotify_id = (String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].spotify_id");

                        if (artistRepository.findArtistBySpotify_id(artist_spotify_id) == null) {
                            artist.setUsername(username);
                            artist.setFirst_name((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].name"));
                            artist.setLast_name((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].name"));
                            artist.setPassword("artistpass");
                            artist.setEmail(username.substring(1, 3) + "@tuned.com");
                            artist.setPhone((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
                            artist.setSpotify_id(artist_spotify_id);
                            artist.setSpotify_url((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].spotify_url"));
                            artist.setImage_url((String) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].artist_details.image_url"));
                            artist.setPopularity((int) JsonPath.read(songJsonResponse, "$.[" + i + "].album.artists[" + j + "].artist_details.popularity"));
                            //set followers
                            //set genres
                        } else {
                            artist = artistRepository.findArtistBySpotify_id(artist_spotify_id);
                        }

                        if (artist.getProducedAlbums() == null) {
                            List<Album> albums = new ArrayList<>();
                            albums.add(album);
                            artist.setProducedAlbums(albums);
                        } else {
                            if (!artist.getProducedAlbums().contains(album))
                                artist.getProducedAlbums().add(album);
                        }
                        if (album.getProducedByArtists() == null) {
                            List<Artist> artists = new ArrayList<>();
                            artists.add(artist);
                            album.setProducedByArtists(artists);
                        } else {
                            if (!album.getProducedByArtists().contains(artist))
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

    @GetMapping("/api/artist/search/{name}")
    public Artist searchArtistByName(@PathVariable("name") String name) {
        Artist artist = new Artist();
        if (artistRepository.findArtistByUsername(name) != null) {
            artist = artistRepository.findArtistByUsername(name);
            System.out.println(artist.getProducedAlbums());
        } else {
            String access_token = fetchToken();
            JSONArray artistJsonResponse = new JSONArray();

            try {
                artistJsonResponse = spotify.searchArtist(access_token, name);
                System.out.println(artistJsonResponse);
                int i = 0;
                String username = JsonPath.read(artistJsonResponse, "$.[" + i + "].name");
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

                int no_albums = JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums.length()");
                List<Album> albums = new ArrayList<>();
                for (int j = 0; j < no_albums; j++) {
                    Album album = new Album();
                    album.setTitle(JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums[" + j + "].title"));
                    album.setSpotify_url(JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums[" + j + "].spotify_url"));
                    album.setSpotify_id(JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums[" + j + "].spotify_id"));
                    album.setImage_url(JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums[" + j + "].image_url"));
                    album.setAlbum_type(JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums[" + j + "].album_type"));
                    String release_date = JsonPath.read(artistJsonResponse, "$.[" + i + "].producedAlbums[" + j + "].release_year");
                    String release_year = release_date.substring(0, 4);
                    album.setRelease_year(release_year);

//                    if(album.getProducedByArtists().isEmpty()){
//                        List<Artist> artists = new ArrayList<>();
//                        artists.add(artist);
//                        album.setProducedByArtists(artists);
//                    }
//                    else{
//                        if(!album.getProducedByArtists().contains(artist)){
//                            album.getProducedByArtists().add(artist);
//                        }
//                    }
                    albumRepository.save(album);
                    System.out.println("success --------------------");
                    albums.add(album);
                }
                if (artist.getProducedAlbums() == null) {
                    artist.setProducedAlbums(albums);
                }
                artistRepository.save(artist);
//                return artistJsonResponse;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return artist;
    }

    @GetMapping("/api/album/search/{title}")
    public List<Album> searchAlbumByTitle(@PathVariable("title") String title){

        List<Album> albums = new ArrayList<>();
//        if(albumRepository.findAlbumByTitle(title) != null){
//             albums = albumRepository.findAlbumByTitle(title);
//        }
        if(1==2){

        }
        else {
            String access_token = fetchToken();
            JSONArray albumJsonResponse = new JSONArray();

            try{
                albumJsonResponse = spotify.searchAlbum(access_token,title);
                int count_albums= JsonPath.read(albumJsonResponse, "$.length()");

                for(int i=1; i< count_albums; i++){
                    Album album = new Album();
                    album.setTitle(JsonPath.read(albumJsonResponse, "$.[" + i + "].title"));
                    album.setSpotify_url(JsonPath.read(albumJsonResponse, "$.[" + i + "].spotify_url"));
                    album.setSpotify_id(JsonPath.read(albumJsonResponse, "$.[" + i + "].spotify_id"));
                    album.setImage_url(JsonPath.read(albumJsonResponse, "$.[" + i + "].image_url"));
                    album.setAlbum_type(JsonPath.read(albumJsonResponse, "$.[" + i + "].album_type"));
                    //add genres to album/////////////////////////
                    String release_date = JsonPath.read(albumJsonResponse, "$.[" + i + "].release_year");
                    String release_year = release_date.substring(0, 4);
                    album.setRelease_year(release_year);

                    List<Artist> artists = new ArrayList<>();
                    int count_artists_in_album= JsonPath.read(albumJsonResponse, "$.[" + i + "]artists.length()");
                    for(int j=1;j<count_artists_in_album; j++){
                        Artist artist = new Artist();
                        String username = JsonPath.read(albumJsonResponse, "$.[" + i + "].artists[" + j + "].name");
                        artist.setUsername(username);
                        artist.setFirst_name((String) JsonPath.read(albumJsonResponse, "$.[" + i + "].artists[" + j + "].name"));
                        artist.setLast_name((String) JsonPath.read(albumJsonResponse, "$.[" + i + "].artists[" + j + "].name"));
                        artist.setPassword("artistpass");
                        artist.setEmail(username.substring(1, 3) + "@tuned.com");
                        artist.setPhone((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
                        artist.setSpotify_id(JsonPath.read(albumJsonResponse, "$.[" + i + "].artists[" + j + "].spotify_url"));
                        artist.setSpotify_url(JsonPath.read(albumJsonResponse, "$.[" + i + "].artists[" + j + "].spotify_url"));
                        artist.setImage_url(JsonPath.read(albumJsonResponse, "$.[" + i + "].artists[" + j + "].artist_details.image_url"));
                        //set followers
                        //set genres

                        if(artist.getProducedAlbums()==null){
                            List<Album> albums_for_artist = new ArrayList<>();
                            albums_for_artist.add(album);
                            artist.setProducedAlbums(albums_for_artist);
                        }
                        else{
                            if(!artist.getProducedAlbums().contains(album))
                                artist.getProducedAlbums().add(album);
                        }

                        artists.add(artist);
                        if(album.getProducedByArtists()==null){
                            album.setProducedByArtists(artists);
                        }
                        else {
                            if(!album.getProducedByArtists().contains(artist))
                                album.getProducedByArtists().add(artist);
                        }
                        artistRepository.save(artist);
                    }


                    albumRepository.save(album);
                    albums.add(album);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return albums;
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