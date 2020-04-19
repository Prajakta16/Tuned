package com.example.Tuned.repository;

import com.example.Tuned.model.Artist;
import com.example.Tuned.model.Listener;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ListenerRepository extends CrudRepository<Listener, Integer> {

    @Query("SELECT lis FROM listener lis WHERE lis.username= :username")
    public Artist findListenerByUsername(@Param("username") String username);

    @Query("SELECT lis FROM artist lis WHERE lis.first_name= :first_name")
    public Artist findListenerByFirstname(@Param("first_name") String first_name);
}
