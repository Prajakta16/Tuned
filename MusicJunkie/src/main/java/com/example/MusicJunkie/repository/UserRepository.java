package com.example.MusicJunkie.repository;

import com.example.MusicJunkie.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT usr FROM user usr where usr.username=: username ")
    public User findUserByUsername(@Param("username") String username);
}
