package com.altf7.sei.validator;

import com.altf7.sei.exception.JogoInvalidException;
import com.altf7.sei.repository.JogoRepository;
import org.springframework.stereotype.Component;

@Component
public class JogoValidator {

    private final JogoRepository jogoRepository;

    public JogoValidator(JogoRepository jogoRepository) {  // ← nome corrigido
        this.jogoRepository = jogoRepository;
    }

    public void validatorNome(String nome) {
        validatorNomeNull(nome);
        validatorNomeTamanho(nome);
    }

    public void validatorNomeNull(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new JogoInvalidException("ERROR: Nome do jogo não pode estar em branco!");
        }
    }

    public void validatorNomeTamanho(String nome) {
        if (nome.length() > 255) {
            throw new JogoInvalidException("ERROR: Nome do jogo não pode ter mais de 255 caracteres!");
        }
    }
}