package com.example.Tuned.repository;

import com.example.Tuned.model.Artist;
import com.example.Tuned.model.Listener;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ListenerRepository extends CrudRepository<Listener, Integer> {

    @Query("SELECT lis FROM listener lis WHERE lis.user_id = :user_id")
    public Listener findListenerByUserId(@Param("user_id") int user_id);

}
