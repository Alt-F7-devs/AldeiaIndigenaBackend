package com.altf7.sei.service;

import com.altf7.sei.dto.presenca.PresencaDetalheDTO;
import com.altf7.sei.dto.presenca.PresencaRelatorioFrequenciaDTO;
import com.altf7.sei.dto.presenca.PresencaResponseDTO;
import com.altf7.sei.entity.Aluno;
import com.altf7.sei.entity.Jogo;
import com.altf7.sei.entity.Presenca;
import com.altf7.sei.repository.AlunoRepository;
import com.altf7.sei.repository.JogoRepository;
import com.altf7.sei.repository.PresencaRepository;
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
    private final JogoRepository jogoRepository;
    private final AlunoService alunoService;

    /* Registra presença de um aluno em um JOGO específico (não mais na sala inteira) */
    @Transactional
    public void registrarPresenca(String cgm, Integer idJogo) {

        Aluno aluno = alunoRepository.findByCgm(cgm)
                .orElseThrow(NotFoundException.AlunoNotFoundException::new);

        Jogo jogo = jogoRepository.findById(idJogo)
                .orElseThrow(NotFoundException.SalaNotFoundException::new);

        // valida se o aluno pertence à sala à qual o jogo está vinculado
        if (jogo.getSala() == null ||
                !alunoService.validarAlunoNaSala(aluno.getId_aluno(), jogo.getSala().getId_sala())) {
            throw new NotFoundException.AlunoNotFoundException();
        }

        // valida se presença já foi registrada para esse jogo
        if (presencaRepository.existsByAlunoEJogo(aluno.getId_aluno(), idJogo)) {
            throw new ConflictException.PresencaJaRegistradaException();
        }

        Presenca presenca = new Presenca();
        presenca.setAluno(aluno);
        presenca.setJogo(jogo);

        presencaRepository.save(presenca);
    }

    /* Frequência de um aluno: presenças registradas / total de jogos da sala dele */
    public PresencaResponseDTO calcularFrequencia(Integer idAluno) {

        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(NotFoundException.AlunoNotFoundException::new);

        double percentual = 0.0;
        if (aluno.getSala() != null) {
            Integer idSala = aluno.getSala().getId_sala();
            long totalJogos = jogoRepository.countBySala(idSala);
            long presencas = presencaRepository.countByAlunoESala(idAluno, idSala);
            percentual = totalJogos == 0 ? 0.0 : (presencas * 100.0) / totalJogos;
        }

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
                    long totalJogos = aluno.getSala() != null
                            ? jogoRepository.countBySala(aluno.getSala().getId_sala())
                            : 0;
                    long totalPresencas = presencaRepository.countByAluno(aluno.getId_aluno());

                    double percentual = totalJogos == 0 ? 0.0 : (totalPresencas * 100.0) / totalJogos;

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

    /* Lista os IDs dos alunos com presença registrada em um jogo específico */
    public List<Integer> listarPresencasDoJogo(Integer idJogo) {
        if (!jogoRepository.existsById(idJogo)) {
            throw new NotFoundException.SalaNotFoundException();
        }
        return presencaRepository.listarIdsAlunosPresentesNoJogo(idJogo);
    }

    /* Remove presença de um aluno em um JOGO específico */
    @Transactional
    public void removerPresenca(String cgm, Integer idJogo) {

        Aluno aluno = alunoRepository.findByCgm(cgm)
                .orElseThrow(NotFoundException.AlunoNotFoundException::new);

        if (!presencaRepository.existsByAlunoEJogo(aluno.getId_aluno(), idJogo)) {
            throw new NotFoundException.AlunoNotFoundException();
        }

        presencaRepository.deleteByAlunoEJogo(aluno.getId_aluno(), idJogo);
    }

    // MÉTODOS RELATÓRIO FREQUÊNCIA (herdam as funcionalidades do metodo: calcularFrequenciaTodos,
    // a fim de permitir que os dados para o relatório não interrompam os relacionamentos que já existem
    private String calcularStatus(double percentual) {
        if (percentual >= 80) return "REGULAR";
        if (percentual >= 75) return "ALERTA";
        return "REPROVADO";
    }

    /* Lista as presenças individuais de um aluno (jogo + data) — usado para conquistas */
    public List<PresencaDetalheDTO> listarPresencasDoAluno(String cgm) {

        Aluno aluno = alunoRepository.findByCgm(cgm)
                .orElseThrow(NotFoundException.AlunoNotFoundException::new);

        return presencaRepository.findByAlunoId(aluno.getId_aluno())
                .stream()
                .map(presenca -> new PresencaDetalheDTO(
                        presenca.getJogo().getId(),
                        presenca.getJogo().getNome(),
                        presenca.getJogo().getDataCriacao()
                ))
                .toList();
    }

    public List<PresencaRelatorioFrequenciaDTO> gerarRelatorioFrequencia() {
        return alunoRepository.findAll()
                .stream()
                .map(aluno -> {
                    Integer idSala = aluno.getSala() != null ? aluno.getSala().getId_sala() : null;

                    long totalJogos = idSala != null ? jogoRepository.countBySala(idSala) : 0;
                    long presencas  = idSala != null
                            ? presencaRepository.countByAlunoESala(aluno.getId_aluno(), idSala)
                            : 0;

                    double percentual = totalJogos == 0 ? 0.0 : (presencas * 100.0) / totalJogos;

                    return new PresencaRelatorioFrequenciaDTO(
                            aluno.getNome(),
                            aluno.getCgm(),
                            idSala,
                            aluno.getSala() != null ? aluno.getSala().getNum_sa() : "Sem sala",
                            presencas,
                            totalJogos,
                            percentual,
                            calcularStatus(percentual)
                    );
                })
                .toList();
    }
}