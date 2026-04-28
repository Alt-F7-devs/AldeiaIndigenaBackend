package com.altf7.sei.service;

import com.altf7.sei.repository.PresencaRepository;
import org.springframework.stereotype.Service;

@Service
public class PresencaService {

    public PresencaRepository presencaRepository;

    public PresencaService(PresencaRepository presencaRepository){
        this.presencaRepository = presencaRepository;
    }
}
