package com.altf7.sei.validator;

import com.altf7.sei.exception.PasswordInvalidException;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {

    public void validatorPassword(String senha) {
        validatorSize(senha);   // já trata null e < 6
        validatorNumber(senha);
        validatorEspecialCaracter(senha);
        validatorLetter(senha);
    }

    private void validatorSize(String senha) {
        if (senha == null || senha.trim().isEmpty() || senha.length() < 6) {
            throw new PasswordInvalidException("ERROR: Senha não pode conter menos que 6 caracteres");
        }
    }

    private void validatorNumber(String senha) {
        if (!senha.matches(".*\\d.*")) {
            throw new PasswordInvalidException("ERROR: Senha deve conter pelo menos 1 número");
        }
    }

    private void validatorLetter(String senha) {
        if (!senha.matches(".*[a-zA-Z].*")) {
            throw new PasswordInvalidException("ERROR: Senha deve conter pelo menos 1 letra");
        }
    }

    private void validatorEspecialCaracter(String senha) {
        if (!senha.matches(".*[@#$%!].*")) {
            throw new PasswordInvalidException("ERROR: Senha deve possuir no mínimo 1 caractere especial");
        }
    }
}
