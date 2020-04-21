package com.example.Tuned.controller;

import com.example.Tuned.spotifyUtility.Spotify;
import com.example.Tuned.model.*;
import com.example.Tuned.repository.AlbumRepository;
import com.example.Tuned.repository.ArtistRepository;
import com.example.Tuned.repository.SongRepository;
import com.example.Tuned.repository.Spotify_tokenRepository;
import com.mysql.cj.xdevapi.JsonArray;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import com.jayway.jsonpath.JsonPath;

import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.*;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Service
public class SpotifyFetchController {

    Spotify spotify = new Spotify();

    @Autowired
    Spotify_tokenRepository spotify_tokenRepository;

    @Autowired
    SpotifySaveController spotifySaveController;

    @Autowired
    SongRepository songRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @GetMapping("/api/song/search/{title}")
    public List<Song> searchSongByTitle(@PathVariable("title") String title) {
        List<Song> songs = new ArrayList<>();
        if (!songRepository.findSongByTitle(title).isEmpty()) {
            songs = songRepository.findSongByTitle(title);
            int count_items = songs.size();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < count_items; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", songs.get(i).getTitle());
                jsonObject.put("spotify_url", songs.get(i).getSpotify_url());
                jsonObject.put("spotify_id", songs.get(i).getSpotify_id());
                jsonObject.put("popularity", songs.get(i).getPopularity());
                jsonObject.put("duration", songs.get(i).getDuration());
                jsonObject.put("preview_url", songs.get(i).getPreview_url());

                Map m = new LinkedHashMap(7);
                m.put("title", songs.get(i).getAlbum().getTitle());
                m.put("spotify_url", songs.get(i).getAlbum().getSpotify_url());
                m.put("spotify_id", songs.get(i).getAlbum().getSpotify_id());
                m.put("image_url", songs.get(i).getAlbum().getImage_url());
                m.put("release_year", songs.get(i).getAlbum().getRelease_year());

                JSONObject albOnject = new JSONObject();
                albOnject.put("album_type", songs.get(i).getAlbum().getAlbum_type());
                albOnject.put("popularity", songs.get(i).getAlbum().getPopularity());
                m.put("album_details",albOnject);

                Set<Artist> artists = songs.get(i).getAlbum().getProducedByArtists();
                JSONArray ja = new JSONArray();
                for(Artist a : artists) {
                    Map mArtist = new LinkedHashMap(2);
                    mArtist.put("name", a.getUsername());
                    mArtist.put("spotify_id", a.getSpotify_id());
                    mArtist.put("spotify_url", a.getSpotify_url());

                    JSONObject artObject = new JSONObject();
                    artObject.put("image_url", a.getImage_url());
                    artObject.put("popularity", a.getPopularity());
                    artObject.put("followers", a.getFollowers());

                    mArtist.put("artist_details", artObject);

                    ja.add(mArtist); // adding map to array
                }
                //jsonObject.put("artists",ja);
                m.put("artists", ja);


                jsonObject.put("album", m); // putting album to JSONObject of song
                jsonArray.add(jsonObject);
            }
            System.out.println(jsonArray);
            return jsonArray;
        } else {
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
                            Set<Album> albums = new HashSet<>();
                            albums.add(album);
                            artist.setProducedAlbums(albums);
                        } else {
                            if (!artist.getProducedAlbums().contains(album))
                                artist.getProducedAlbums().add(album);
                        }
                        if (album.getProducedByArtists() == null) {
                            Set<Artist> artists = new HashSet<>();
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

    @GetMapping("/api/album/search/{title}")
    public List<Album> searchAlbumByTitle(@PathVariable("title") String title) {
        List<Album> albums = new ArrayList<>();
        if (!albumRepository.findAlbumByTitle(title).isEmpty()) {
            System.out.println("Album exists in db");
            albums = albumRepository.findAlbumByTitle(title);

            JSONArray jsonArray = new JSONArray();
            for(Album alb : albums) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("title", alb.getTitle());
                jsonObject.put("spotify_url", alb.getSpotify_url());
                jsonObject.put("spotify_id", alb.getSpotify_id());
                jsonObject.put("album_type", alb.getAlbum_type());
                jsonObject.put("image_url", alb.getImage_url());
                jsonObject.put("release_year", alb.getRelease_year());

                JSONArray artistJsonArray = new JSONArray();
                Set<Artist> artists = alb.getProducedByArtists();
                for (Artist art : artists) {
                    JSONObject artistObj = new JSONObject();
                    artistObj.put("name", art.getUsername());
                    artistObj.put("spotify_id", art.getSpotify_id());
                    artistObj.put("spotify_url",art.getSpotify_url());

                    JSONObject artist_details = new JSONObject();
                    artist_details.put("followers", art.getFollowers());
                    artist_details.put("image_url", art.getImage_url());
                    artist_details.put("popularity", art.getPopularity());
                    artistObj.put("artist_details", artist_details);

                    artistJsonArray.add(artistObj); // adding map to array
                }
                jsonObject.put("artists", artistJsonArray);

                JSONArray songsJsonArray = new JSONArray();
                List<Song> songs = alb.getSongs();
                for (Song son : songs) {
                    JSONObject songObj = new JSONObject();
                    songObj.put("title", son.getTitle());
                    songObj.put("spotify_id", son.getSpotify_id());
                    songObj.put("spotify_url",son.getSpotify_url());
                    songObj.put("preview_url", son.getPreview_url());
                    songObj.put("duration",son.getDuration());

                    songsJsonArray.add(songObj); // adding map to array
                }
                jsonObject.put("songs", songsJsonArray);

                jsonArray.add(jsonObject);
            }
            return jsonArray;
        }
        else {
            System.out.println("Album does not exist in db, fetching through spotify");
            String access_token = fetchToken();
            JSONArray albumJsonResponse = new JSONArray();

            try{
                albumJsonResponse = spotify.searchAlbum(access_token,title);
                System.out.println(albumJsonResponse);
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
                            Set<Album> albums_for_artist = new HashSet<>();
                            albums_for_artist.add(album);
                            artist.setProducedAlbums(albums_for_artist);
                        }
                        else{
                            if(!artist.getProducedAlbums().contains(album))
                                artist.getProducedAlbums().add(album);
                        }

                        if(album.getProducedByArtists()==null){
                            Set<Artist> artistsInAlbum = new HashSet<>();
                            artistsInAlbum.add(artist);
                            album.setProducedByArtists(artistsInAlbum);
                        }
                        else {
                            if(!album.getProducedByArtists().contains(artist))
                                album.getProducedByArtists().add(artist);
                        }
                        artistRepository.save(artist);
                    }

                    List<Song> songs = new ArrayList<>();
                    int count_songs_in_album= JsonPath.read(albumJsonResponse, "$.[" + i + "]songs.length()");
                    for(int j=1;j<count_songs_in_album; j++){
                        Song song = new Song();

                        song.setTitle(JsonPath.read(albumJsonResponse, "$.[" + i + "].songs[" + j + "].title"));
                        song.setSpotify_id(JsonPath.read(albumJsonResponse, "$.[" + i + "].songs[" + j + "].spotify_url"));
                        song.setSpotify_url(JsonPath.read(albumJsonResponse, "$.[" + i + "].songs[" + j + "].spotify_url"));
                        song.setPreview_url(JsonPath.read(albumJsonResponse, "$.[" + i + "].songs[" + j + "].preview_url"));
                        song.setDuration(JsonPath.read(albumJsonResponse, "$.[" + i + "].songs[" + j + "].duration"));

                        if(song.getAlbum()!=album){
                            song.setAlbum(album);
                        }
                        if(album.getSongs()== null){
                            List<Song> songsInAlbum = new ArrayList<>();
                            songsInAlbum.add(song);
                            album.setSongs(songsInAlbum);
                        }
                        else{
                            if(!album.getSongs().contains(song))
                                album.getSongs().add(song);
                        }
                        songRepository.save(song);
                    }

                    albumRepository.save(album);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return albumJsonResponse;
        }
    }

    @GetMapping("/api/artist/search/{name}")
    public List<Artist> searchArtistByName(@PathVariable("name") String name) {

        List<Artist> artists = new ArrayList<>();

        if (artistRepository.findArtistByUsername(name).isEmpty()) {
            spotifySaveController.saveArtistInDb(name);
        }
        else{
            artists = artistRepository.findArtistByUsername(name);
        }
        return artists;
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