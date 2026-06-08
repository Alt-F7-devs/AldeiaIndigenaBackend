package com.altf7.sei.service;

import com.altf7.sei.dto.aluno.AlunoResponseDTO;
import com.altf7.sei.dto.jogo.JogoResponseDTO;
import com.altf7.sei.dto.sala.SalaListResponseDTO;
import com.altf7.sei.dto.sala.SalaRequestDTO;
import com.altf7.sei.entity.*;
import com.altf7.sei.exception.*;
import com.altf7.sei.repository.*;
import com.altf7.sei.validator.ValidadorCredentialsExceptionSala;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;
    private final EntityManager entityManager;
    private final AdminRepository adminRepository;
    private final JogoRepository jogoRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final ValidadorCredentialsExceptionSala validadorCredentialsExceptionSala;
    private final JogoService jogoService;
    private final AlunoService alunoService;

    /* Cria Sala */
    @Transactional
    public Sala criarSala(SalaRequestDTO req) {
        validadorCredentialsExceptionSala.validarNumSa(req.num_sa());
        try {
            Admin admin = adminRepository.findById(req.admin_id())
                    .orElseThrow(AdminInvalidException.AdminNotFoundExceptionAll::new);

            Sala sala = new Sala();
            sala.setNum_sa(req.num_sa());
            sala.setAdmin(admin);

            if (req.jogo_id_jogo() != null) {
                Jogo jogo = jogoRepository.findById(req.jogo_id_jogo())
                        .orElseThrow(JogoInvalidException.JogoNotFoundExceptionAll::new);
                sala.setJogo(jogo);
            }

            if (req.professor_id_professor() != null) {
                Professor professor = professorRepository.findById(req.professor_id_professor())
                        .orElseThrow(ProfessorInvalidException.ProfessorNotFoundExceptionAll::new);
                sala.setProfessor(professor);
            }

            return entityManager.merge(sala);
        } catch (DataAccessException ex) {
            throw new InternalServerError.SalaInternalServerError();
        }
    }

    /* Edita Sala (parcial - apenas campos enviados nao-nulos) */
    @Transactional
    public Sala editarSala(Integer id_sala, SalaRequestDTO req) {
        Sala sala = salaRepository.findById(id_sala)
                .orElseThrow(SalaInvalidException.SalaNotFoundExceptionAll::new);
        try {
            if (req.num_sa() != null) {
                validadorCredentialsExceptionSala.validarNumSa(req.num_sa());
                sala.setNum_sa(req.num_sa());
            }

            if (req.admin_id() != null) {
                Admin admin = adminRepository.findById(req.admin_id())
                        .orElseThrow(AdminInvalidException.AdminNotFoundExceptionAll::new);
                sala.setAdmin(admin);
            }

            if (req.jogo_id_jogo() != null) {
                Jogo jogo = jogoRepository.findById(req.jogo_id_jogo())
                        .orElseThrow(JogoInvalidException.JogoNotFoundExceptionAll::new);
                sala.setJogo(jogo);
            }

            if (req.professor_id_professor() != null) {
                Professor professor = professorRepository.findById(req.professor_id_professor())
                        .orElseThrow(ProfessorInvalidException.ProfessorNotFoundExceptionAll::new);
                sala.setProfessor(professor);
            }

            return entityManager.merge(sala);
        } catch (DataAccessException ex) {
            throw new InternalServerError.SalaInternalServerError();
        }
    }

    /* Desvincula o professor da Sala (set null) */
    @Transactional
    public Sala desvincularProfessorSala(Integer id_sala) {
        Sala sala = salaRepository.findById(id_sala)
                .orElseThrow(SalaInvalidException.SalaNotFoundExceptionAll::new);
        try {
            sala.setProfessor(null);
            return salaRepository.save(sala);
        } catch (DataAccessException ex) {
            throw new InternalServerError.SalaInternalServerError();
        }
    }

    /* Note: FAZER HANDLER + EXCEPTION */
    /* Adiciona Aluno (cadastrado) na Sala (cadastrada) onde o professor/admin autenticado vai estar */
    @Transactional
    public Aluno addAlunoSala(Integer id_aluno, Integer id_sala) {

        try {
            Aluno aluno = alunoRepository.findById(id_aluno)
                    .orElseThrow(AlunoInvalidException.AlunoNotFoundExceptionAll::new);

            Sala sala = salaRepository.findById(id_sala)
                    .orElseThrow(SalaInvalidException.SalaNotFoundExceptionAll::new);

            aluno.setSala(sala);
            return alunoRepository.save(aluno);

        } catch (DataAccessException ex) {
            throw new InternalServerError.SalaAlunoInternalServerError();
        }
    }

    /* Recebe metodo --> listar Aluno geral da Classe AlunoService */
    public List<AlunoResponseDTO> listarAlunoSala() {
        return alunoService.listarAluno();
    }

    /* Recebe metodo --> listar Aluno por ID da Classe AlunoService */
    public List<AlunoResponseDTO> listarAlunoPorIdSala(Integer id_aluno) {
        return alunoService.listarAlunoPorId(id_aluno);
    }

    /* Listagem Geral de Salas cadastradas */
    public List<SalaListResponseDTO> listarSala(){
        try {
           return salaRepository.findAll()
                    .stream()
                    .map(sala -> new SalaListResponseDTO(sala.getId_sala(),sala.getNum_sa(),sala.getData(),sala.getProfessor() != null ? sala.getProfessor().getId_professor() : null,sala.getProfessor() != null ? sala.getProfessor().getNome() : null))
                    .toList();

        } catch (DataAccessException ex) {
            throw new InternalServerError.SalaListInternalServerError();
        }
    }

    /* Listagem de Sala por ID*/
    public List<SalaListResponseDTO> listarSalaPorId(Integer id_sala){
        try {
            return salaRepository.findById(id_sala)
                    .stream()
                    .map(sala -> new SalaListResponseDTO(sala.getId_sala(),sala.getNum_sa(),sala.getData(),sala.getProfessor() != null ? sala.getProfessor().getId_professor() : null,sala.getProfessor() != null ? sala.getProfessor().getNome() : null))
                    .toList();
        } catch (DataAccessException ex) {
            throw new InternalServerError.SalaListInternalServerError();
        }
    }

    /* Deletar Sala */
    @Transactional
    public void deletarSala(Integer id_sala) {
        Sala sala = salaRepository.findById(id_sala)
                .orElseThrow(JogoInvalidException.JogoNotFoundExceptionAll::new);
        salaRepository.save(sala);
    }

    /* Adiciona jogo (cadastrado) na sala */
    @Transactional
    public Sala addJogoSala(Integer id_jogo, Integer id_sala) {
        try {
            Jogo jogo = jogoRepository.findById(id_jogo)
                    .orElseThrow(JogoInvalidException.JogoNotFoundExceptionAll::new);

            Sala sala = salaRepository.findById(id_sala)
                    .orElseThrow(SalaInvalidException.SalaNotFoundExceptionAll::new);

            sala.setJogo(jogo);
            return salaRepository.save(sala);

        } catch (DataAccessException ex) {
            throw new InternalServerError.SalaJogoInternalServerError();
        }
    }

    /* Recebe metodo --> listar jogo geral da Classe JogoService */
    public List<JogoResponseDTO> listarJogoSala() {
      return jogoService.listar();
    }

    /* Recebe metodo --> listar jogo por ID da Classe JogoService */
    public JogoResponseDTO buscarJogoPorIdSala(Integer id) {
        return jogoService.buscarPorId(id);
    }
}