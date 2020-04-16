package com.example.Tuned.controller;

import com.example.Tuned.model.Listener;
import com.example.Tuned.repository.ListenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ListenerController {

    @Autowired
    ListenerRepository listenerRepository;

    @PostMapping("/api/listener")
    Listener createListener(@RequestBody  Listener listener){
        return listenerRepository.save(listener);
    }
}
