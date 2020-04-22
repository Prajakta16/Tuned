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

@CrossOrigin(origins = "*", allowedHeaders = "*")
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
        if (songRepository.findSongByTitle(title).isEmpty()) {
            spotifySaveController.saveSongInDb(title);
        }

        songs = songRepository.findSongByTitle(title);
        int count_items = songs.size();
        JSONArray jsonArray = new JSONArray();

        if (songs != null)
            for (int i = 0; i < count_items; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("song_id", songs.get(i).getSong_id());
                jsonObject.put("title", songs.get(i).getTitle());
                jsonObject.put("spotify_url", songs.get(i).getSpotify_url());
                jsonObject.put("spotify_id", songs.get(i).getSpotify_id());
                jsonObject.put("popularity", songs.get(i).getPopularity());
                jsonObject.put("duration", songs.get(i).getDuration());
                jsonObject.put("preview_url", songs.get(i).getPreview_url());

                Map m = new LinkedHashMap(7);
                m.put("album_id", songs.get(i).getAlbum().getAlbum_id());
                m.put("title", songs.get(i).getAlbum().getTitle());
                m.put("spotify_url", songs.get(i).getAlbum().getSpotify_url());
                m.put("spotify_id", songs.get(i).getAlbum().getSpotify_id());
                m.put("image_url", songs.get(i).getAlbum().getImage_url());
                m.put("release_year", songs.get(i).getAlbum().getRelease_year());
                m.put("album_type", songs.get(i).getAlbum().getAlbum_type());
                m.put("popularity", songs.get(i).getAlbum().getPopularity());

                JSONObject albOnject = new JSONObject();
                albOnject.put("album_type", songs.get(i).getAlbum().getAlbum_type());
                albOnject.put("popularity", songs.get(i).getAlbum().getPopularity());
                m.put("album_details", albOnject);

                Set<Artist> artists = songs.get(i).getAlbum().getProducedByArtists();
                JSONArray ja = new JSONArray();
                if (artists != null) {
                    for (Artist a : artists) {
                        Map mArtist = new LinkedHashMap(2);
                        mArtist.put("artist_id", a.getUser_id());
                        mArtist.put("name", a.getUsername());
                        mArtist.put("spotify_id", a.getSpotify_id());
                        mArtist.put("spotify_url", a.getSpotify_url());
                        mArtist.put("image_url", a.getImage_url());
                        mArtist.put("popularity", a.getPopularity());
                        mArtist.put("followers", a.getFollowers());

                        JSONObject artObject = new JSONObject();
                        artObject.put("image_url", a.getImage_url());
                        artObject.put("popularity", a.getPopularity());
                        artObject.put("followers", a.getFollowers());

                        mArtist.put("artist_details", artObject);

                        ja.add(mArtist); // adding map to array
                    }
                    m.put("artists", ja);
                }

                jsonObject.put("album", m); // putting album to JSONObject of song
                jsonArray.add(jsonObject);
            }
        return jsonArray;
    }

    @GetMapping("/api/album/search/{title}")
    public List<Album> searchAlbumByTitle(@PathVariable("title") String title) {
        List<Album> albums = new ArrayList<>();

        if (albumRepository.findAlbumByTitle(title).isEmpty()) {
            spotifySaveController.saveAlbumInDb(title);
        }

        albums = albumRepository.findAlbumByTitle(title);
        JSONArray jsonArray = new JSONArray();
        for (Album alb : albums) {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("album_id", alb.getAlbum_id());
            jsonObject.put("title", alb.getTitle());
            jsonObject.put("spotify_url", alb.getSpotify_url());
            jsonObject.put("spotify_id", alb.getSpotify_id());
            jsonObject.put("album_type", alb.getAlbum_type());
            jsonObject.put("image_url", alb.getImage_url());
            jsonObject.put("release_year", alb.getRelease_year());

            JSONArray artistJsonArray = new JSONArray();
            Set<Artist> artists = alb.getProducedByArtists();
            if (alb.getProducedByArtists() != null) {
                for (Artist art : artists) {
                    JSONObject artistObj = new JSONObject();
                    artistObj.put("artist_id", art.getUser_id());
                    artistObj.put("name", art.getUsername());
                    artistObj.put("spotify_id", art.getSpotify_id());
                    artistObj.put("spotify_url", art.getSpotify_url());

                    JSONObject artist_details = new JSONObject();
                    artist_details.put("followers", art.getFollowers());
                    artist_details.put("image_url", art.getImage_url());
                    artist_details.put("popularity", art.getPopularity());
                    artistObj.put("artist_details", artist_details);
                    artistObj.put("followers", art.getFollowers());
                    artistObj.put("image_url", art.getImage_url());
                    artistObj.put("popularity", art.getPopularity());

                    artistJsonArray.add(artistObj); // adding map to array
                }
            }
            jsonObject.put("artists", artistJsonArray);

            JSONArray songsJsonArray = new JSONArray();
            List<Song> songs = alb.getSongs();
            if (alb.getSongs() != null) {
                for (Song son : songs) {
                    JSONObject songObj = new JSONObject();
                    songObj.put("song_id", son.getSong_id());
                    songObj.put("title", son.getTitle());
                    songObj.put("spotify_id", son.getSpotify_id());
                    songObj.put("spotify_url", son.getSpotify_url());
                    songObj.put("preview_url", son.getPreview_url());
                    songObj.put("duration", son.getDuration());

                    songsJsonArray.add(songObj); // adding map to array
                }
            }
            jsonObject.put("songs", songsJsonArray);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @GetMapping("/api/artist/search/{name}")
    public List<Artist> searchArtistByName(@PathVariable("name") String name) {

        List<Artist> artists = new ArrayList<>();

        if (artistRepository.findArtistByUsername(name).isEmpty()) {
            spotifySaveController.saveArtistInDb(name);
        }
        artists = artistRepository.findArtistByUsername(name);
        return artists;
    }

}