package com.altf7.sei.service;

import com.altf7.sei.repository.AlunoRepository;
import org.springframework.stereotype.Service;

@Service
public class AlunoService {

    public AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository){
        this.alunoRepository = alunoRepository;
    }

    

}
