package com.example.Tuned.repository;

import com.example.Tuned.model.Listener_activity;
import com.example.Tuned.model.Song;
import com.example.Tuned.model.Listener;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Listener_activityRepository extends CrudRepository<Listener_activity, Integer> {

    @Query("SELECT la FROM listener_activity la where la.listener=:listener and la.song=:song")
    public Listener_activity findActivityByListenerAndSong(@Param("listener") Listener listener, @Param("song") Song song);

    @Query("SELECT la FROM listener_activity la where la.listener=:listener")
    public Listener_activity findActivityByListener(@Param("listener") Listener listener);

    @Query("SELECT la.song FROM listener_activity la where la.listener= :listener")
    public List<Song> findSongByListener(@Param("listener") Listener listener);

    @Query("SELECT la.song FROM listener_activity la where la.listener= :listener AND la.likes = :true")
    public List<Song> findLikedSongByListener(@Param("listener") Listener listener);

    @Query("SELECT la.song FROM listener_activity la where la.listener= :listener AND la.is_favourite = :true")
    public List<Song> findFavSongByListener(@Param("listener") Listener listener);

    @Query("SELECT la.song FROM listener_activity la where la.listener= :listener AND la.dislikes = :true")
    public List<Song> findDislikedSongByListener(@Param("listener") Listener listener);

//    @Query("SELECT la.song FROM listener_activity la where la.listener= :listener")
//    public List<Song> findSongByListenerId(@Param("listener_id") int listener_id);
}
