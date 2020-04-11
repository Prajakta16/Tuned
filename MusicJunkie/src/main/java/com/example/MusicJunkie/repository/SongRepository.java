package com.example.MusicJunkie.repository;

import com.example.MusicJunkie.model.Song;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongRepository extends CrudRepository<Song, Integer> {

    @Query("SELECT s FROM song s where s.title=: title ")
    public Song findSongByTitle(@Param("title") String title);

    @Query("SELECT s FROM song s where s.genre=: genre ")
    public List<Song> findSongByGenre(@Param("genre") String genre);
}
