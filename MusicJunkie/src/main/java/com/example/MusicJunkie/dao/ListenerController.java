package com.example.MusicJunkie.dao;

import com.example.MusicJunkie.model.Listener;
import com.example.MusicJunkie.repository.ListenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListenerController {

    @Autowired
    ListenerRepository listenerRepository;

    Listener createListener(Listener listener){
        return listenerRepository.save(listener);
    }
}
