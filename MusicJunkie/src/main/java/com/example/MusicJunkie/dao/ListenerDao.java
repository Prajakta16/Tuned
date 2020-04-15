package com.example.MusicJunkie.dao;

import com.example.MusicJunkie.model.Listener;
import com.example.MusicJunkie.model.Playlist;
import com.example.MusicJunkie.repository.ListenerRepository;
import com.example.MusicJunkie.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListenerDao {

    @Autowired
    ListenerRepository listenerRepository;

    @Autowired
    PlaylistRepository playlistRepository;

    public Listener createListener(Listener listener){
        return listenerRepository.save(listener);
    }

    public List<Listener> findAllListeners(){
        return (List<Listener>) listenerRepository.findAll();
    }

}
