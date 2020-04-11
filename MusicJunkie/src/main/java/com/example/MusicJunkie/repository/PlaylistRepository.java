package com.example.MusicJunkie.repository;

import com.example.MusicJunkie.model.Playlist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PlaylistRepository extends CrudRepository<Playlist,Integer> {

    @Query("SELECT ply FROM playlist ply where ply.title=: title ")
    public Playlist findPlaylistByTitle(@Param("title") String title);
}
