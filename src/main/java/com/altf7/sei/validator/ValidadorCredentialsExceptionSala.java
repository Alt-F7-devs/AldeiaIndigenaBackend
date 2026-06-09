package com.altf7.sei.validator;

import com.altf7.sei.exception.NumSaInvalidException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorCredentialsExceptionSala {

    public void validarNumSa(String numSa) {
        validarNulo(numSa);
        validarCaracteresInvalidos(numSa);
        validarQuantidadeLetras(numSa);
        validarQuantidadeNumeros(numSa);
    }

    public void validarNulo(String numSa) {
        if (numSa == null || numSa.isBlank()) {
            throw new NumSaInvalidException("ERROR: num_sa não pode estar em branco!");
        }
    }

    public void validarCaracteresInvalidos(String numSa) {
        if (!numSa.matches("^[a-zA-Z0-9]+$")) {
            throw new NumSaInvalidException("ERROR: num_sa não pode conter caracteres especiais ou números negativos!");
        }
    }

    public void validarQuantidadeLetras(String numSa) {
        long totalLetras = numSa.chars().filter(Character::isLetter).count();
        if (totalLetras > 1) {
            throw new NumSaInvalidException("ERROR: num_sa deve conter no máximo 1 letra!");
        }
    }

    public void validarQuantidadeNumeros(String numSa) {
        long totalNumeros = numSa.chars().filter(Character::isDigit).count();
        if (totalNumeros > 1) {
            throw new NumSaInvalidException("ERROR: num_sa deve conter no máximo 1 número!");
        }
    }
}