package com.example.Tuned.controller;

import com.example.Tuned.model.*;
import com.example.Tuned.repository.ListenerRepository;
import com.example.Tuned.repository.Listener_activityRepository;
import com.example.Tuned.repository.SongRepository;
import com.example.Tuned.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Service
public class Listener_activityController {

    @Autowired
    Listener_activityRepository listener_activityRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ListenerRepository listenerRepository;

    //   6. Like a song
    //   7. Dislike a song
    //   8. Mark song as favorite
    //   9. Increase views on a song
    //  10. comments a song

    /*

    @GetMapping("api/user/{user_id}/likes/{song_id}")
    public Listener_activity likeASongById(@PathVariable("user_id") int user_id, @PathVariable("song_id") int song_id) {
        if (userRepository.findById(user_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Song song = songRepository.findById(song_id).get();
            Listener listener = listenerRepository.findById(user_id).get();
            Listener_activity listener_activity = listener_activityRepository.findActivityByUser(user_id, song_id);
            if (listener_activity != null) {
                Boolean songlike = listener_activity.getLikes();
                if (!songlike)
                {
                    listener_activity.setLikes(Boolean.parseBoolean("True"));
                    listener_activity.setDislikes(Boolean.parseBoolean("False"));
                    return listener_activityRepository.save(listener_activity);
                }
            } else {
                Listener_activity nlistener_activity = new Listener_activity(listener, song);
                nlistener_activity.setLikes(Boolean.parseBoolean("True"));
                return listener_activityRepository.save(nlistener_activity);
            }
        }
        return null;
    }

    @GetMapping("api/user/{user_id}/dislikes/{song_id}")
    public Listener_activity dislikeASongById(@PathVariable("user_id") int user_id, @PathVariable("song_id") int song_id) {
        if (userRepository.findById(user_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Song song = songRepository.findById(song_id).get();
            Listener listener = listenerRepository.findById(user_id).get();
            Listener_activity listener_activity = listener_activityRepository.findActivityByUser(user_id, song_id);
            if (listener_activity != null) {
                Boolean songdislike = listener_activity.getDislikes();
                if (!songdislike)
                {
                    listener_activity.setDislikes(Boolean.parseBoolean("True"));
                    listener_activity.setLikes(Boolean.parseBoolean("False"));
                    return listener_activityRepository.save(listener_activity);
                }
            } else {
                Listener_activity nlistener_activity = new Listener_activity(listener, song);
                nlistener_activity.setDislikes(Boolean.parseBoolean("True"));
                return listener_activityRepository.save(nlistener_activity);
            }
        }
        return null;
    }

    @GetMapping("api/user/{user_id}/favourites/{song_id}")
    public Listener_activity favASongById(@PathVariable("user_id") int user_id, @PathVariable("song_id") int song_id) {
        if (userRepository.findById(user_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Song song = songRepository.findById(song_id).get();
            Listener listener = listenerRepository.findById(user_id).get();
            Listener_activity listener_activity = listener_activityRepository.findActivityByUser(user_id, song_id);
            if (listener_activity != null) {
                Boolean songfav = listener_activity.isIs_favourite();
                if (!songfav)
                {
                    listener_activity.setIs_favourite(Boolean.parseBoolean("True"));
                    return listener_activityRepository.save(listener_activity);
                }
            } else {
                Listener_activity nlistener_activity = new Listener_activity(listener, song);
                nlistener_activity.setIs_favourite(Boolean.parseBoolean("True"));
                return listener_activityRepository.save(nlistener_activity);
            }
        }
        return null;
    }

    @GetMapping("api/user/{user_id}/visits/{song_id}")
    public Listener_activity visASongById(@PathVariable("user_id") int user_id, @PathVariable("song_id") int song_id) {
        if (userRepository.findById(user_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Song song = songRepository.findById(song_id).get();
            Listener listener = listenerRepository.findById(user_id).get();
            Listener_activity listener_activity = listener_activityRepository.findActivityByUser(user_id, song_id);
            if (listener_activity != null) {
                int songvis = listener_activity.getVisits();
                songvis = songvis + 1;
                listener_activity.setVisits(songvis);
                return listener_activityRepository.save(listener_activity);
            }
            else {
                Listener_activity nlistener_activity = new Listener_activity(listener, song);
                nlistener_activity.setVisits(1);
                return listener_activityRepository.save(nlistener_activity);
            }
        }
        return null;
    }

    @PostMapping("api/user/{user_id}/comments/{song_id/{comments}")
    public Listener_activity commentASongById(@PathVariable("user_id") int user_id, @PathVariable("song_id") int song_id, @PathVariable("comments") String comments) {
        if (userRepository.findById(user_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Song song = songRepository.findById(song_id).get();
            Listener listener = listenerRepository.findById(user_id).get();
            Listener_activity listener_activity = listener_activityRepository.findActivityByUser(user_id, song_id);
            if (listener_activity != null) {
                String songcomm = listener_activity.getComment();
                songcomm = comments;
                listener_activity.setComment(songcomm);
                return listener_activityRepository.save(listener_activity);
            }
            else {
                Listener_activity nlistener_activity = new Listener_activity(listener, song);
                nlistener_activity.setComment(comments);
                return listener_activityRepository.save(nlistener_activity);
            }
        }
        return null;
    }

     */

}
