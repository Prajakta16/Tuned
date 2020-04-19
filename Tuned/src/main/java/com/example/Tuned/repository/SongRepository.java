package com.example.Tuned.repository;

import com.example.Tuned.model.Album;
import com.example.Tuned.model.Song;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends CrudRepository<Song, Integer> {

    @Query("SELECT s FROM song s where s.title=: title ")
    public List<Song> findSongByTitle(@Param("title") String title);

}
