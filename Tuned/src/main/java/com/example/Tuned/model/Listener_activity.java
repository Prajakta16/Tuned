package com.example.Tuned.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "listener_activity")
public class Listener_activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int listener_activity_id;

    @ManyToOne
    @JsonIgnore
    private Listener listener;
    public void setListener(Listener listener) {
        this.listener = listener;
        if(!listener.getSongActivity().contains(this)) {
            listener.getSongActivity().add(this);
        }
    }

    @ManyToOne
    @JsonIgnore
    private Song song;
//    public void setSong(Song song) {
//        this.song = song;
//        if(!song.getListenerActivity().contains(this)) {
//            song.getListenerActivity().add(this);
//        }
//    }

    private String comment;

    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean likes;

    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean dislikes;

    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean is_favourite;

    @Column(columnDefinition = "int(3) DEFAULT 0")
    private int visits;

    public Listener_activity(Listener listener, Song song) {
        this.listener = listener;
        this.song = song;
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
}
