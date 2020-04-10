package com.example.MusicJunkie.model;

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

    @ManyToOne
    @JsonIgnore
    private Song song;

    private String comment;

    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean likes;

    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean dislikes;

    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean is_favourite;

    @Column(columnDefinition = "int(3) DEFAULT 0")
    private int visits;

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

    public void setLikes(boolean likes) {
        this.likes = likes;
    }

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
}
