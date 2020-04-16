package com.example.Tuned.dao;

import com.example.Tuned.model.Listener;
import com.example.Tuned.repository.ListenerRepository;
import com.example.Tuned.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
