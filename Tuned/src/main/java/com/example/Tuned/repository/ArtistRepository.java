package com.example.Tuned.repository;

import com.example.Tuned.model.Artist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends CrudRepository<Artist,Integer> {

    @Query("SELECT art FROM artist art WHERE art.username= :username")
    public Artist findArtistByUsername(@Param("username") String username);

    @Query("SELECT art FROM artist art WHERE art.first_name= :first_name")
    public Artist findArtistByFirstname(@Param("first_name") String first_name);
}
