package com.example.Tuned.controller;

import com.example.Tuned.model.*;
import com.example.Tuned.repository.ListenerRepository;
import com.example.Tuned.repository.Listener_activityRepository;
import com.example.Tuned.repository.SongRepository;
import com.example.Tuned.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @PostMapping("/api/listener/{listener_id}/likes/song/{song_id}")
    public Listener_activity listenerPerformsLikeActivityOnSong(@PathVariable("listener_id") int listener_id, @PathVariable("song_id") int song_id, @RequestBody JSONObject likeJson) {
        if (listenerRepository.findById(listener_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Boolean like_value = JsonPath.read(likeJson, "$.like");
            Listener listener = listenerRepository.findById(listener_id).get();
            Song song = songRepository.findById(song_id).get();
            Listener_activity la;
            if (listener_activityRepository.findActivityByListenerAndSong(listener, song) != null){
                la = new Listener_activity();
                la.setListener(listener);
                la.setSong(song);
                la.setLikes(like_value);
            }
            else{
                la = listener_activityRepository.findActivityByListenerAndSong(listener,song);
                la.setLikes(like_value);
            }
            listener_activityRepository.save(la);
            listenerRepository.save(listener);
            songRepository.save(song);
        }
        return null;
    }

    @PostMapping("/api/user/{listener_id}/likes/{song_id}")
    public Listener_activity likeASongById(@PathVariable("listener_id") int listener_id, @PathVariable("song_id") int song_id) {
        if (listenerRepository.findById(listener_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Listener_activity la;
            Listener listener = listenerRepository.findById(listener_id).get();
            Song song = songRepository.findById(song_id).get();
            if (listener_activityRepository.findActivityByListenerAndSong(listener, song) != null) { //an activity exists
                la = listener_activityRepository.findActivityByListenerAndSong(listener, song);
                if (la.getLikes() == true)  //check if user has already liked a song
                {
                    la.setLikes(false);     //if so the clicking like will unlike it
                } else {
                    la.setLikes(true);     //else set like as true and unlike as false
                    la.setDislikes(false);
                }
            } else {
                la = new Listener_activity();
                la.setSong(song);
                la.setListener(listener);
                la.setLikes(true);
                la.setDislikes(false);
                if (!listener.getListener_activities().contains(la))
                    listener.getListener_activities().add(la);
                if (!song.getActivities().contains(la))
                    song.getActivities().add(la);
            }
            listener_activityRepository.save(la);
            listenerRepository.save(listener);
            songRepository.save(song);
            return la;
        }
        //System.out.println("listener or song not present");
        return null;
    }

    @PostMapping("/api/user/{listener_id}/dislikes/{song_id}")
    public Listener_activity dislikeASongById(@PathVariable("listener_id") int listener_id, @PathVariable("song_id") int song_id) {
        if (listenerRepository.findById(listener_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Listener_activity la;
            Listener listener = listenerRepository.findById(listener_id).get();
            Song song = songRepository.findById(song_id).get();
            if (listener_activityRepository.findActivityByListenerAndSong(listener, song) != null) { //an activity exists
                la = listener_activityRepository.findActivityByListenerAndSong(listener, song);
                if (la.getDislikes() == true) {
                    la.setDislikes(false);
                } else {
                    la.setDislikes(true);
                    la.setLikes(false);
                }
            } else {
                la = new Listener_activity();
                la.setSong(song);
                la.setListener(listener);
                la.setDislikes(true);
                la.setLikes(false);
                if (!listener.getListener_activities().contains(la))
                    listener.getListener_activities().add(la);
                if (!song.getActivities().contains(la))
                    song.getActivities().add(la);
            }
            listener_activityRepository.save(la);
            listenerRepository.save(listener);
            songRepository.save(song);
            return la;
        }
        //System.out.println("listener or song not present");
        return null;
    }

    @PostMapping("/api/user/{listener_id}/favourite/{song_id}")
    public Listener_activity favoriteASongById(@PathVariable("listener_id") int listener_id, @PathVariable("song_id") int song_id) {
        if (listenerRepository.findById(listener_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Listener_activity la;
            Listener listener = listenerRepository.findById(listener_id).get();
            Song song = songRepository.findById(song_id).get();
            if (listener_activityRepository.findActivityByListenerAndSong(listener, song) != null) { //an activity exists
                la = listener_activityRepository.findActivityByListenerAndSong(listener, song);
                if (la.isIs_favourite() == true) //check if already added to fav
                {
                    la.setIs_favourite(false); //clicking again will unfav
                } else {
                    la.setIs_favourite(true); //else fav
                }
            } else {
                la = new Listener_activity();
                la.setSong(song);
                la.setListener(listener);
                la.setIs_favourite(true);
                if (!listener.getListener_activities().contains(la))
                    listener.getListener_activities().add(la);
                if (!song.getActivities().contains(la))
                    song.getActivities().add(la);
            }
            listener_activityRepository.save(la);
            listenerRepository.save(listener);
            songRepository.save(song);
            return la;
        }
        //System.out.println("listener or song not present");
        return null;
    }

    @PostMapping("/api/user/{listener_id}/song/{song_id}/comment/{comments}")
    public Listener_activity commentASongById(@PathVariable("listener_id") int listener_id, @PathVariable("song_id") int song_id, @PathVariable("comments") String comments) {
        if (listenerRepository.findById(listener_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Listener_activity la;
            Listener listener = listenerRepository.findById(listener_id).get();
            Song song = songRepository.findById(song_id).get();
            if (listener_activityRepository.findActivityByListenerAndSong(listener, song) != null) { //an activity exists
                la = listener_activityRepository.findActivityByListenerAndSong(listener, song);
                la.setComment(comments);
            } else {
                la = new Listener_activity();
                la.setSong(song);
                la.setListener(listener);
                la.setComment(comments);
                if (!listener.getListener_activities().contains(la))
                    listener.getListener_activities().add(la);
                if (!song.getActivities().contains(la))
                    song.getActivities().add(la);
            }
            listener_activityRepository.save(la);
            listenerRepository.save(listener);
            songRepository.save(song);
            return la;
        }
        //System.out.println("listener or song not present");
        return null;
    }

    @PostMapping("/api/user/{listener_id}/visits/{song_id}")
    public Listener_activity visitASongById(@PathVariable("listener_id") int listener_id, @PathVariable("song_id") int song_id) {
        if (listenerRepository.findById(listener_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Listener_activity la;
            Listener listener = listenerRepository.findById(listener_id).get();
            Song song = songRepository.findById(song_id).get();
            if (listener_activityRepository.findActivityByListenerAndSong(listener, song) != null) { //an activity exists
                la = listener_activityRepository.findActivityByListenerAndSong(listener, song);
                int songvis = la.getVisits();
                songvis = songvis + 1;
                la.setVisits(songvis);
            } else {
                la = new Listener_activity();
                la.setSong(song);
                la.setListener(listener);
                la.setVisits(1);
                if (!listener.getListener_activities().contains(la))
                    listener.getListener_activities().add(la);
                if (!song.getActivities().contains(la))
                    song.getActivities().add(la);
            }
            listener_activityRepository.save(la);
            listenerRepository.save(listener);
            songRepository.save(song);
            return la;
        }
        //System.out.println("listener or song not present");
        return null;
    }

    @GetMapping("/api/listener/{listener_id}/likes/all")
    public List<Song> getLikedSongs(int listener_id) {
        Listener listener = listenerRepository.findById(listener_id).get();
        List<Song> likedSongs = listener_activityRepository.findLikedSongByListener(listener);
        return likedSongs;
    }

    @GetMapping("/api/listener/{listener_id}/favourite/all")
    public List<Song> getFavSongs(int listener_id) {
        Listener listener = listenerRepository.findById(listener_id).get();
        List<Song> likedSongs = listener_activityRepository.findFavSongByListener(listener);
        return likedSongs;
    }

    @GetMapping("/api/listener/{listener_id}/dislikes/all")
    public List<Song> getDislikedSongs(int listener_id) {
        Listener listener = listenerRepository.findById(listener_id).get();
        List<Song> likedSongs = listener_activityRepository.findDislikedSongByListener(listener);
        return likedSongs;
    }

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
