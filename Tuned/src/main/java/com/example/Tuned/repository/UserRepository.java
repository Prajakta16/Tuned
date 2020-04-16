package com.example.Tuned.repository;

import com.example.Tuned.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT usr FROM user usr where usr.username=: username ")
    public User findUserByUsername(@Param("username") String username);

    @Query("SELECT usr FROM user usr where usr.first_name=: first_name ")
    public User findUserByFirst_name(@Param("first_name") String first_name);
}
