package com.altf7.sei.service;

import com.altf7.sei.dto.presenca.PresencaResponseDTO;
import com.altf7.sei.entity.Aluno;
import com.altf7.sei.entity.Presenca;
import com.altf7.sei.entity.Sala;
import com.altf7.sei.repository.AlunoRepository;
import com.altf7.sei.repository.PresencaRepository;
import com.altf7.sei.repository.SalaRepository;
import com.altf7.sei.exception.ConflictException;
import com.altf7.sei.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresencaService {

    private final PresencaRepository presencaRepository;
    private final AlunoRepository alunoRepository;
    private final SalaRepository salaRepository;
    private final AlunoService alunoService;

    public void registrarPresenca(String cgm, Integer idSala) {

        Aluno aluno = alunoRepository.findByCgm(cgm)
                .orElseThrow(NotFoundException.AlunoNotFoundException::new);
        // valida se o aluno pertence àquela sala
        if (!alunoService.validarAlunoNaSala(aluno.getId_aluno(), idSala)) {
            throw new NotFoundException.AlunoNotFoundException();
        }

        // valida se presença já foi registrada
        if (presencaRepository.existsByAlunoESala(aluno.getId_aluno(), idSala)) {
            throw new ConflictException.PresencaJaRegistradaException();
        }


        Sala sala = salaRepository.findById(idSala)
                .orElseThrow(NotFoundException.SalaNotFoundException::new);

        Presenca presenca = new Presenca();
        presenca.setAluno(aluno);
        presenca.setSala(sala);

        presencaRepository.save(presenca);
    }

    public PresencaResponseDTO calcularFrequencia(Integer idAluno) {

        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(NotFoundException.AlunoNotFoundException::new);


        boolean temPresenca = aluno.getSala() != null &&
                presencaRepository.existsByAlunoESala(idAluno, aluno.getSala().getId_sala());

        double percentual = temPresenca ? 100.0 : 0.0;

        String status;
        if (percentual >= 80) {
            status = "REGULAR";
        } else if (percentual >= 75) {
            status = "ALERTA";
        } else {
            status = "REPROVADO";
        }

        return new PresencaResponseDTO(
                aluno.getNome(),
                aluno.getCgm(),
                aluno.getSala() != null ? aluno.getSala().getNum_sa() : "Sem sala",
                percentual,
                status
        );
    }

    public List<PresencaResponseDTO> calcularFrequenciaTodos() {
        return alunoRepository.findAll()
                .stream()
                .map(aluno -> {
                    long totalSalas = alunoRepository.countSalasByAluno(aluno.getId_aluno());
                    long totalPresencas = presencaRepository.countByAluno(aluno.getId_aluno());

                    double percentual = totalSalas == 0 ? 0.0 : (totalPresencas * 100.0) / totalSalas;

                    String status;
                    if (percentual >= 80) {
                        status = "REGULAR";
                    } else if (percentual >= 75) {
                        status = "ALERTA";
                    } else {
                        status = "REPROVADO";
                    }

                    return new PresencaResponseDTO(
                            aluno.getNome(),
                            aluno.getCgm(),
                            aluno.getSala() != null ? aluno.getSala().getNum_sa() : "Sem sala",
                            percentual,
                            status
                    );
                })
                .toList();
    }

    @Transactional
    public void removerPresenca(String cgm, Integer idSala) {

        Aluno aluno = alunoRepository.findByCgm(cgm)
                .orElseThrow(NotFoundException.AlunoNotFoundException::new);

        if (!presencaRepository.existsByAlunoESala(aluno.getId_aluno(), idSala)) {
            throw new NotFoundException.AlunoNotFoundException();
        }

        presencaRepository.deleteByAlunoESala(aluno.getId_aluno(), idSala);
    }
}