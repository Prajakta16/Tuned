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

import java.util.Iterator;
import java.util.List;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Service
public class Listener_activityController {

    @Autowired
    Listener_activityRepository listener_activityRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    ListenerRepository listenerRepository;

    @PostMapping("/api/listener/{listener_id}/likes/song/{song_id}")
    public Listener_activity listenerPerformsLikeActivityOnSong(@PathVariable("listener_id") int listener_id, @PathVariable("song_id") int song_id, @RequestBody JSONObject likeJson) {
        if (listenerRepository.findById(listener_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Boolean like_value = JsonPath.read(likeJson, "$.like");
            Listener_activity la = getListenerActivity(listener_id, song_id);
            la.setLikes(like_value);
            listener_activityRepository.save(la);
            return la;
        }
        return null;
    }

    @PostMapping("/api/listener/{listener_id}/dislikes/song/{song_id}")
    public Listener_activity listenerPerformsDislikeActivityOnSong(@PathVariable("listener_id") int listener_id, @PathVariable("song_id") int song_id, @RequestBody JSONObject dislikeJson) {
        if (listenerRepository.findById(listener_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Boolean dislike_value = JsonPath.read(dislikeJson, "$.dislike");
            Listener_activity la = getListenerActivity(listener_id, song_id);
            la.setDislikes(dislike_value);
            listener_activityRepository.save(la);
            return la;
        }
        return null;
    }

    @PostMapping("/api/listener/{listener_id}/fav/song/{song_id}")
    public Listener_activity listenerPerformsFavActivityOnSong(@PathVariable("listener_id") int listener_id, @PathVariable("song_id") int song_id, @RequestBody JSONObject favJson) {
        if (listenerRepository.findById(listener_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            Boolean fav_value = JsonPath.read(favJson, "$.favourite");
            Listener_activity la = getListenerActivity(listener_id, song_id);
            la.setIs_favourite(fav_value);
            listener_activityRepository.save(la);
            return la;
        }
        return null;
    }

    @PostMapping("/api/listener/{listener_id}/comment/song/{song_id}")
    public Listener_activity listenerPerformsCommentActivityOnSong(@PathVariable("listener_id") int listener_id, @PathVariable("song_id") int song_id, @RequestBody JSONObject commentJson) {
        if (listenerRepository.findById(listener_id).isPresent() && songRepository.findById(song_id).isPresent()) {
            String comment = JsonPath.read(commentJson, "$.comment");
            Listener_activity la = getListenerActivity(listener_id, song_id);
            la.setComment(comment);
            listener_activityRepository.save(la);
            return la;
        }
        return null;
    }

    @GetMapping("/api/song/liked/by/{listener_id}")
    public List<Song> getLikedSongForAUser(@PathVariable("listener_id") int listener_id) {
        List<Song> Final = newArrayList();
        if (listenerRepository.findById(listener_id).isPresent()) {
            Listener listener = listenerRepository.findById(listener_id).get();
            List<Song> songs = listener_activityRepository.findLikedSongByListener(listener);
            return songs;
        }
        return null;
    }

    @GetMapping("/api/song/disliked/by/{listener_id}")
    public List<Song> getDislikedSongForAUser(@PathVariable("listener_id") int listener_id) {
        List<Song> Final = newArrayList();
        if (listenerRepository.findById(listener_id).isPresent()) {
            Listener listener = listenerRepository.findById(listener_id).get();
            return listener_activityRepository.findDislikedSongByListener(listener);
        }
        return null;
    }

    @GetMapping("/api/song/favourite/by/{listener_id}")
    public List<Song> getFavouriteSongForAUser(@PathVariable("listener_id") int listener_id) {
        List<Song> Final = newArrayList();
        if (listenerRepository.findById(listener_id).isPresent()) {
            Listener listener = listenerRepository.findById(listener_id).get();
            return listener_activityRepository.findFavSongByListener(listener);
        }
        return null;
    }


//    @GetMapping("/api/song/liked/by/{listener_id}")
//    public List<Song> getLikedSongForAUser(@PathVariable("listener_id") int listener_id) {
//        List<Song> Final = newArrayList();
//        if (listenerRepository.findById(listener_id).isPresent()) {
//            Listener listener = listenerRepository.findById(listener_id).get();
//            List<Song> song = listener_activityRepository.findSongByListener(listener);
//            Iterator<Song> iter = song.iterator();
//            while (iter.hasNext()) {
//                Song songs = iter.next();
//                Listener_activity la = listener_activityRepository.findActivityByListenerAndSong(listener, songs);
//                if (la.getLikes() == Boolean.TRUE) {
//                    Final.add(songs);
//                }
//            }
//            return Final;
//        }
//        return null;
//    }

//    @GetMapping("/api/song/disliked/by/{listener_id}")
//    public List<Song> getDislikedSongForAUser(@PathVariable("listener_id") int listener_id) {
//        List<Song> Final = newArrayList();
//        if (listenerRepository.findById(listener_id).isPresent()) {
//            Listener listener = listenerRepository.findById(listener_id).get();
//            List<Song> song = listener_activityRepository.findSongByListener(listener);
//            Iterator<Song> iter = song.iterator();
//            while (iter.hasNext()) {
//                Song songs = iter.next();
//                Listener_activity la = listener_activityRepository.findActivityByListenerAndSong(listener, songs);
//                if (la.getDislikes() == Boolean.TRUE) {
//                    Final.add(songs);
//                }
//
//            }
//            return Final;
//        }
//        return null;
//    }
//
//    @GetMapping("/api/song/favourite/by/{listener_id}")
//    public List<Song> getFavouriteSongForAUser(@PathVariable("listener_id") int listener_id) {
//        List<Song> Final = newArrayList();
//        if (listenerRepository.findById(listener_id).isPresent()) {
//            Listener listener = listenerRepository.findById(listener_id).get();
//            List<Song> song = listener_activityRepository.findSongByListener(listener);
//            Iterator<Song> iter = song.iterator();
//            while (iter.hasNext()) {
//                Song songs = iter.next();
//                Listener_activity la = listener_activityRepository.findActivityByListenerAndSong(listener, songs);
//                if (la.isIs_favourite() == Boolean.TRUE) {
//                    Final.add(songs);
//                }
//
//            }
//            return Final;
//        }
//        return null;
//    }

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

    public Listener_activity getListenerActivity(int listener_id, int song_id) {
        Listener listener = listenerRepository.findById(listener_id).get();
        Song song = songRepository.findById(song_id).get();
        Listener_activity la;
        if (listener_activityRepository.findActivityByListenerAndSong(listener, song) == null) {
            la = new Listener_activity();
            la.setListener(listener);
            la.setSong(song);
            la.setUsername(listener.getUsername());
            la.setListener_id(listener.getUser_id());
            songRepository.save(song);
            listener_activityRepository.save(la);
        } else {
            la = listener_activityRepository.findActivityByListenerAndSong(listener, song);
        }
        return la;
    }
}
