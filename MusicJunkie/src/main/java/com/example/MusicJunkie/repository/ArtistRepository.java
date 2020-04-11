package com.example.MusicJunkie.repository;

import com.example.MusicJunkie.model.Artist;
import org.springframework.data.repository.CrudRepository;

public interface ArtistRepository extends CrudRepository<Artist,Integer> {
}
