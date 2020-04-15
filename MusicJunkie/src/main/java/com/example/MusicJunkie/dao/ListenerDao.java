package com.example.MusicJunkie.dao;

import com.example.MusicJunkie.model.Listener;
import com.example.MusicJunkie.repository.ListenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListenerDao {

    @Autowired
    ListenerRepository listenerRepository;

    public Listener createListener(Listener listener){
        return listenerRepository.save(listener);
    }

    public List<Listener> findAllListeners(){
        return (List<Listener>) listenerRepository.findAll();
    }
}
