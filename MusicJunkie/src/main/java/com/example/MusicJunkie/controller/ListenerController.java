package com.example.MusicJunkie.controller;

import com.example.MusicJunkie.model.Listener;
import com.example.MusicJunkie.repository.ListenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Service
public class ListenerController {

    @Autowired
    ListenerRepository listenerRepository;

    @PostMapping("/api/listener")
    Listener createListener(@RequestBody  Listener listener){
        return listenerRepository.save(listener);
    }
}
