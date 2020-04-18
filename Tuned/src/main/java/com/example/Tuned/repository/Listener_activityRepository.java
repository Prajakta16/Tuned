package com.example.Tuned.repository;

import com.example.Tuned.model.Listener_activity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Listener_activityRepository extends CrudRepository<Listener_activity, Integer> {

//    @Query("SELECT l FROM listener_activity l where l.user_id=: user_id and l.song_id =: song_id")
//    public Listener_activity findActivityByUser(@Param("user_id") int user_id, @Param("song_id") int song_id);

}
