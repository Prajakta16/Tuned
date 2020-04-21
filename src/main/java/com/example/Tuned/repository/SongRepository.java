package com.example.Tuned.repository;

import com.example.Tuned.model.Album;
import com.example.Tuned.model.Artist;
import com.example.Tuned.model.Song;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends CrudRepository<Song, Integer> {

//    public String QUERY = "SELECT art FROM artist art WHERE art.username like CONCAT( '%' ,CONCAT(?1, '%'))";
//    @Query(QUERY)
//    public List<Artist> findArtistByUsername(@Param("username") String username);

    @Query("SELECT s FROM song s where s.title like CONCAT( '%' ,CONCAT(?1, '%')) ")
    public List<Song> findSongByTitle(@Param("title") String title);

    @Query("SELECT s.album FROM song s WHERE s.song_id = :song_id")
    public Album findAlbumForSong(@Param("song_id") int song_id);


}
