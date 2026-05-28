package com.altf7.sei.validator;
import com.altf7.sei.exception.CgmInvalidException;
import com.altf7.sei.exception.PasswordInvalidException;
import com.altf7.sei.repository.AlunoRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidatorCredentialsExceptionAluno {

    private final AlunoRepository alunoRepository;
    public ValidatorCredentialsExceptionAluno(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public void validatorCgm(String cgm) {
        validatorNull(cgm);
        validatorSizeCgm(cgm);
        validatorExistsCgm(cgm);
    }

    public void validatorNull(String cgm) {
        if (cgm == null) {
            throw new CgmInvalidException("ERROR: Campo não pode estar em branco!");
        }
    }

    public void validatorSizeCgm(String cgm) {
        if(cgm == null || cgm.length() !=10) {
            throw new CgmInvalidException("ERROR: CGM deve conter 10 caracteres");
        }
    }

    public void validatorExistsCgm(String cgm){
        boolean exists = alunoRepository.findByCgm(cgm).isPresent();
        if(exists){
            throw new CgmInvalidException("ERROR: CGM já cadastrado no sistema!");
        }
    }
}