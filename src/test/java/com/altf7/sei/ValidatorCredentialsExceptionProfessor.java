package com.altf7.sei.validator;

import com.altf7.sei.exception.CpfInvalidException;
import com.altf7.sei.repository.ProfessorRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidatorCredentialsExceptionProfessor {

    private final ProfessorRepository professorRepository;

    public ValidatorCredentialsExceptionProfessor(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public void validatorCpf(String cpf) {
        validarInfoCpf(cpf);
    }

    public void validarInfoCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new CpfInvalidException("ERROR: CPF não pode estar em branco");
        }

        String cpfLimpo = cpf.replaceAll("\\D", "");

        if (cpfLimpo.length() != 11) {
            throw new CpfInvalidException("ERROR: CPF deve conter 11 dígitos");
        }
        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            throw new CpfInvalidException("ERROR: CPF inválido");
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpfLimpo.charAt(i) - '0') * (10 - i);
        }
        int dig1 = 11 - (soma % 11);
        if (dig1 >= 10) dig1 = 0;

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpfLimpo.charAt(i) - '0') * (11 - i);
        }
        int dig2 = 11 - (soma % 11);
        if (dig2 >= 10) dig2 = 0;

        if (dig1 != (cpfLimpo.charAt(9) - '0') || dig2 != (cpfLimpo.charAt(10) - '0')) {
            throw new CpfInvalidException("ERROR: CPF inválido");
        }

        if (professorRepository.findByCpf(cpfLimpo).isPresent()) {
            throw new CpfInvalidException("ERROR: CPF já cadastrado no sistema!");
        }
    }
}
