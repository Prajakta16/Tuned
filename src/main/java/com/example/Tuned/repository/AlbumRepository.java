package com.example.Tuned.repository;

import com.example.Tuned.model.Album;
import com.example.Tuned.model.Artist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends CrudRepository<Album,Integer> {

    @Query("SELECT alb FROM album alb where alb.title = :title ")
    public List<Album> findAlbumByTitle(@Param("title") String title);

    @Query("SELECT alb FROM album alb WHERE alb.spotify_id = :spotify_id")
    public Album findAlbumBySpotifyId(@Param("spotify_id") String spotify_id);

    public List<Artist> getArtistsForAlbum(@Param("album") Album album);

}
