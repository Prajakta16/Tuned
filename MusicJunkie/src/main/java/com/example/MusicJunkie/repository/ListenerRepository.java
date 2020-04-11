package com.example.MusicJunkie.repository;

import com.example.MusicJunkie.model.Listener;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListenerRepository extends CrudRepository<Listener, Integer> {
}
