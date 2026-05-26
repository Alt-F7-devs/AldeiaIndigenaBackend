package com.altf7.sei.validator;

import com.altf7.sei.exception.PasswordInvalidException;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {

    public void validatorPassword(String senha) {
        validatorSize(senha);
        validatorNumber(senha);
        validatorEspecialCaracter(senha);
        validatorNull(senha);
    }

    private void validatorSize(String senha) {
        if(senha == null || senha.length() < 6) {
            throw new PasswordInvalidException("ERROR: Senha não pode conter menos que 6 caracteres");
        }
    }

    private void validatorNumber(String senha) {
        if(!senha.matches(".*\\d.*")) {
            throw new PasswordInvalidException("ERROR: Senha deve conter pelo menos 1 número");
        }
    }

    private void validatorEspecialCaracter(String senha) {
        if(!senha.matches(".*[@#$%!].*")) {
            throw new PasswordInvalidException("ERROR: Senha deve possuir no mínimo 1 caractere especial");
        }
    }

    private void validatorNull(String senha) {
        if (senha.isBlank() || senha == null) {
            throw new PasswordInvalidException("ERROR: Campo não pode estar em branco!");
        }
    }

}
