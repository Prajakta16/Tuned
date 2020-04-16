package com.example.Tuned.repository;

import com.example.Tuned.model.Listener_activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Listener_activityRepository extends CrudRepository<Listener_activity, Integer> {
}
