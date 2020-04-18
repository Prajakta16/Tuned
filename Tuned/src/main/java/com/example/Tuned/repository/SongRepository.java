package com.example.Tuned.repository;

import com.example.Tuned.model.Song;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends CrudRepository<Song, Integer> {

    @Query("SELECT s FROM song s where s.title=: title ")
    public Song findSongByTitle(@Param("title") String title);

    @Query("SELECT s FROM song s where s.genre=: genre ")
    public List<Song> findSongByGenre(@Param("genre") String genre);


    //@Query("SELECT s FROM song s where s.album_id=: album_id")
    //public List<Song> findSongByAlbum(@Param("album_id") int album_id);

    //@Query("SELECT s FROM song s where s.artist_id=: artist_id")
    //public List<Song> findSongByArtist(@Param("artist_id") int artist_id);

}
