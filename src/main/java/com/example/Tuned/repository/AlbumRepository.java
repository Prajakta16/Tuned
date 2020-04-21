package com.example.Tuned.repository;

import com.example.Tuned.model.Album;
import com.example.Tuned.model.Artist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AlbumRepository extends CrudRepository<Album,Integer> {

    @Query("SELECT alb FROM album alb where alb.title like CONCAT( '%' ,CONCAT(?1, '%')) ")
    public List<Album> findAlbumByTitle(@Param("title") String title);

    @Query("SELECT alb FROM album alb WHERE alb.spotify_id = :spotify_id")
    public Album findAlbumBySpotifyId(@Param("spotify_id") String spotify_id);

    @Query("SELECT a.producedByArtists FROM album a WHERE a.album_id = :album_id")
    public Set<Artist> findArtistDetailsForAlbum(@Param("album_id") int album_id);

}
