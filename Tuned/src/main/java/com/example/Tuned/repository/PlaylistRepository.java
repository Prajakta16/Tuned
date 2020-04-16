package com.example.Tuned.repository;

import com.example.Tuned.model.Playlist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist,Integer> {

    @Query("SELECT ply FROM playlist ply where ply.title=: title ")
    public List<Playlist> findPlaylistByTitle(@Param("title") String title);
}
