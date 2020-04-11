package com.example.MusicJunkie.repository;

import com.example.MusicJunkie.model.Album;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends CrudRepository<Album,Integer> {

    @Query("SELECT alb FROM album alb where alb.title=: title ")
    public Album findAlbumByTitle(@Param("title") String title);

}
