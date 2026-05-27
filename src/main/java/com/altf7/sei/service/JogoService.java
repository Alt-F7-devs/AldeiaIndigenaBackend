package com.altf7.sei.service;

import com.altf7.sei.repository.JogoRepository;
import org.springframework.stereotype.Service;

@Service
public class JogoService {

    public JogoRepository jogoRepository;

    public JogoService(JogoRepository jogoRepository){
        this.jogoRepository = jogoRepository;
    }
}
