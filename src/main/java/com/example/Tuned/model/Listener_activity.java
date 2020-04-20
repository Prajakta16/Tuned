package com.example.Tuned.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "listener_activity")
@Table(name = "listener_activity")
public class Listener_activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int listener_activity_id;

    private String comment;
    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean likes;
    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean dislikes;
    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean is_favourite;
    @Column(columnDefinition = "int(3) DEFAULT 0")
    private int visits;
    private String username;
    private int listener_id;

    @ManyToOne
    @JsonIgnore
    private Listener listener;

    @ManyToOne
    @JsonIgnore
    private Song song;

    public Listener_activity(int listener_activity_id, Listener listener, Song song, String comment, boolean likes, boolean dislikes, boolean is_favourite, int visits) {
        this.listener_activity_id = listener_activity_id;
        this.listener = listener;
        this.song = song;
        this.comment = comment;
        this.likes = likes;
        this.dislikes = dislikes;
        this.is_favourite = is_favourite;
        this.visits = visits;
    }

    public Listener_activity() {

    }

    public void setSong(Song song) {
        this.song = song;
        if(song.getActivities() == null){
            List<Listener_activity> activities = new ArrayList<>();
            activities.add(this);
            song.setActivities(activities);
        }
        else{
            if(!song.getActivities().contains(this)) {
                song.getActivities().add(this);
            }
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
        if(listener.getListener_activities() == null){
            List<Listener_activity> activities = new ArrayList<>();
            activities.add(this);
            listener.setListener_activities(activities);
        }
        else{
            if(!listener.getListener_activities().contains(this)) {
                listener.getListener_activities().add(this);
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getListener_activity_id() {
        return listener_activity_id;
    }

    public void setListener_activity_id(int listener_activity_id) {
        this.listener_activity_id = listener_activity_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isLikes() {
        return likes;
    }

    public Boolean getLikes() { return likes; }
    public void setLikes(boolean likes) {
        this.likes = likes;
    }
    public Boolean getDislikes() { return dislikes; }
    public boolean isDislikes() {
        return dislikes;
    }

    public void setDislikes(boolean dislikes) {
        this.dislikes = dislikes;
    }

    public boolean isIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(boolean is_favourite) {
        this.is_favourite = is_favourite;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public Song getSong() {
        return song;
    }

    public User getListener() { return listener; }

    public int getListener_id() {
        return listener_id;
    }

    public void setListener_id(int listener_id) {
        this.listener_id = listener_id;
    }
}
