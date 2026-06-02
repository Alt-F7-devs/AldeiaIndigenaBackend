package com.altf7.sei.service;

import com.altf7.sei.dto.jogo.JogoRequestDTO;
import com.altf7.sei.dto.jogo.JogoResponseDTO;
import com.altf7.sei.entity.Admin;
import com.altf7.sei.entity.Jogo;
import com.altf7.sei.exception.JogoInvalidException;
import com.altf7.sei.repository.AdminRepository;
import com.altf7.sei.repository.JogoRepository;
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

    public List<JogoResponseDTO> listar(){
        return jogoRepository.findAll()
                .stream()
                .map(JogoResponseDTO::from)
                .toList();
    }

    public JogoResponseDTO buscarporid(Integer id){
        return jogoRepository.findById(id)
                .map(JogoResponseDTO::from)
                .orElseThrow(() -> new JogoInvalidException.JogoNotFoundException(id));
    }

    public JogoResponseDTO criar(JogoRequestDTO request) {
        jogoValidator.validatorNome(request.nome());
        Admin admin = adminRepository.findById(request.admin_login())
                .orElseThrow(() -> new RuntimeException("Admin não encontrado"));
        Jogo jogo = new Jogo();
        jogo.setNome(request.nome());
        jogo.setAdmin(admin);

        return JogoResponseDTO.from(jogoRepository.save(jogo));
    }

    public JogoResponseDTO atualizar(Integer id, JogoRequestDTO request) {
        jogoValidator.validatorNome(request.nome());
        Jogo jogo = jogoRepository.findById(id)
                .orElseThrow(() -> new JogoInvalidException.JogoNotFoundException(id));
        jogo.setNome(request.nome());
        return JogoResponseDTO.from(jogoRepository.save(jogo));
    }

    public void deletar(Integer id) {
        if (!jogoRepository.existsById(id)) {
            throw new JogoInvalidException.JogoNotFoundException(id);
        }
        jogoRepository.deleteById(id);
    }
}