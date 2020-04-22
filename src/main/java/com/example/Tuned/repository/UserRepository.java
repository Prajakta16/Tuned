package com.example.Tuned.repository;

import com.example.Tuned.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT usr.following FROM user usr WHERE usr.user_id = :user_id")
    public List<User> findFollowing(@Param("user_id") int user_id);

    @Query("SELECT usr.followers FROM user usr WHERE usr.user_id = :user_id")
    public List<User> findFollowers(@Param("user_id") int user_id);

//    @Modifying
//    @Transactional
//    @Query("DELETE from user u")
//    void deleteAllUsers();

}