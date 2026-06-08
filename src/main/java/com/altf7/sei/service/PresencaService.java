package com.altf7.sei.service;

import com.altf7.sei.repository.PresencaRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresencaService {

    public PresencaRepository presencaRepository;

    public PresencaService(PresencaRepository presencaRepository){
        this.presencaRepository = presencaRepository;
    }
}