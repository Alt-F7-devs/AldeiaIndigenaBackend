package com.altf7.sei.service;

import com.altf7.sei.dto.jogo.JogoRequestDTO;
import com.altf7.sei.dto.jogo.JogoResponseDTO;
import com.altf7.sei.dto.jogo.JogoResumoDTO;
import com.altf7.sei.entity.Admin;
import com.altf7.sei.entity.Jogo;
import com.altf7.sei.entity.Sala;
import com.altf7.sei.exception.AccessDeniedCustomException;
import com.altf7.sei.exception.AdminInvalidException;
import com.altf7.sei.exception.JogoInvalidException;
import com.altf7.sei.exception.SalaInvalidException;
import com.altf7.sei.repository.AdminRepository;
import com.altf7.sei.repository.JogoRepository;
import com.altf7.sei.repository.SalaRepository;
import com.altf7.sei.validator.JogoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JogoService {

    private final JogoRepository jogoRepository;
    private final JogoValidator jogoValidator;
    private final AdminRepository adminRepository;
    private final SalaRepository salaRepository;
    private final AuthContextService authContextService;

    /* Garante que o professor logado tem permissão sobre o jogo informado.
       Admin sempre passa. Professor só passa se o jogo estiver vinculado
       a alguma sala dele. */
    private void verificarPermissao(Integer idJogo) {
        if (authContextService.isAdmin()) {
            return;
        }
        Integer idProfessor = authContextService.getIdProfessorLogado();
        if (!jogoRepository.pertenceAoProfessor(idJogo, idProfessor)) {
            throw new AccessDeniedCustomException();
        }
    }

    /* Listar jogos já cadastrados (GERAL).
       Admin vê todos. Professor vê apenas os jogos vinculados às suas salas. */
    public List<JogoResponseDTO> listar(){
        if (authContextService.isAdmin()) {
            return jogoRepository.findAll()
                    .stream()
                    .map(JogoResponseDTO::from)
                    .toList();
        }

        Integer idProfessor = authContextService.getIdProfessorLogado();
        return jogoRepository.listarResumoPorProfessor(idProfessor)
                .stream()
                .map(r -> new JogoResponseDTO(r.id_jogo(), r.nome()))
                .toList();
    }

    /* Listar jogos já cadastrados (ID) */
    public JogoResponseDTO buscarPorId(Integer id){
        verificarPermissao(id);
        return jogoRepository.findById(id)
                .map(JogoResponseDTO::from)
                .orElseThrow(() -> new JogoInvalidException.JogoNotFoundException(id));
    }

    /* Listar jogos com data de criação e contagem de alunos que fizeram (histórico).
       Admin vê todos. Professor vê apenas os jogos vinculados às suas salas. */
    public List<JogoResumoDTO> listarResumo() {
        if (authContextService.isAdmin()) {
            return jogoRepository.listarResumo();
        }
        Integer idProfessor = authContextService.getIdProfessorLogado();
        return jogoRepository.listarResumoPorProfessor(idProfessor);
    }

    /* Listar histórico de jogos de uma sala específica.
       Professor só pode consultar salas que são dele; Admin pode qualquer sala. */
    public List<JogoResumoDTO> listarResumoPorSala(Integer idSala) {
        if (authContextService.isAdmin()) {
            return jogoRepository.listarResumoPorSala(idSala);
        }
        Integer idProfessor = authContextService.getIdProfessorLogado();
        return jogoRepository.listarResumoPorSalaEProfessor(idSala, idProfessor);
    }

    /* Cria Jogo. Se id_sala for informado, o jogo já nasce vinculado a ela
       (professor só pode vincular a uma sala que é dele; Admin pode qualquer). */
    public JogoResponseDTO criarJogo(JogoRequestDTO request) {
        jogoValidator.validatorNome(request.nome());
        Admin admin = adminRepository.findById(request.admin_login())
                .orElseThrow(AdminInvalidException.AdminNotFoundExceptionAll::new);
        Jogo jogo = new Jogo();
        jogo.setNome(request.nome());
        jogo.setAdmin(admin);

        if (request.id_sala() != null) {
            Sala sala = salaRepository.findById(request.id_sala())
                    .orElseThrow(SalaInvalidException.SalaNotFoundExceptionAll::new);

            if (!authContextService.isAdmin()) {
                Integer idProfessor = authContextService.getIdProfessorLogado();
                boolean salaEhDoProfessor = sala.getProfessor() != null
                        && sala.getProfessor().getId_professor().equals(idProfessor);
                if (!salaEhDoProfessor) {
                    throw new AccessDeniedCustomException();
                }
            }

            jogo.setSala(sala);
        }

        return JogoResponseDTO.from(jogoRepository.save(jogo));
    }

    /* Atualizar dados de Jogo */
    public JogoResponseDTO atualizarJogo(Integer id, JogoRequestDTO request) {
        verificarPermissao(id);
        jogoValidator.validatorNome(request.nome());
        Jogo jogo = jogoRepository.findById(id)
                .orElseThrow(() -> new JogoInvalidException.JogoNotFoundException(id));
        jogo.setNome(request.nome());
        return JogoResponseDTO.from(jogoRepository.save(jogo));
    }

    /* Deletar Jogo */
    public void deletarJogo(Integer id) {
        verificarPermissao(id);
        if (!jogoRepository.existsById(id)) {
            throw new JogoInvalidException.JogoNotFoundException(id);
        }
        jogoRepository.deleteById(id);
    }
}