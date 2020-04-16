package com.example.Tuned.repository;

import com.example.Tuned.model.Listener;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListenerRepository extends CrudRepository<Listener, Integer> {
}
