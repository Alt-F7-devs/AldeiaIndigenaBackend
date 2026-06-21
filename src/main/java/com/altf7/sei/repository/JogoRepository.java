package com.altf7.sei.repository;

import com.altf7.sei.dto.jogo.JogoResumoDTO;
import com.altf7.sei.entity.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Integer> {

    /* Resumo de TODOS os jogos (uso restrito a ADMIN) */
    @Query("""
    SELECT new com.altf7.sei.dto.jogo.JogoResumoDTO(
        j.id,
        j.nome,
        j.dataCriacao,
        COUNT(p.aluno),
        s.id_sala
    )
    FROM Jogo j
    LEFT JOIN j.sala s
    LEFT JOIN Presenca p ON p.jogo = j
    GROUP BY j.id, j.nome, j.dataCriacao, s.id_sala
    ORDER BY j.dataCriacao DESC
""")
    List<JogoResumoDTO> listarResumo();

    /* Resumo apenas dos jogos vinculados a salas do professor informado */
    @Query("""
    SELECT new com.altf7.sei.dto.jogo.JogoResumoDTO(
        j.id,
        j.nome,
        j.dataCriacao,
        COUNT(p.aluno),
        s.id_sala
    )
    FROM Jogo j
    JOIN j.sala s
    LEFT JOIN Presenca p ON p.jogo = j
    WHERE s.professor.id_professor = :idProfessor
    GROUP BY j.id, j.nome, j.dataCriacao, s.id_sala
    ORDER BY j.dataCriacao DESC
""")
    List<JogoResumoDTO> listarResumoPorProfessor(@Param("idProfessor") Integer idProfessor);

    /* Resumo dos jogos vinculados a uma sala específica (sem filtro de professor; uso ADMIN) */
    @Query("""
    SELECT new com.altf7.sei.dto.jogo.JogoResumoDTO(
        j.id,
        j.nome,
        j.dataCriacao,
        COUNT(p.aluno),
        s.id_sala
    )
    FROM Jogo j
    JOIN j.sala s
    LEFT JOIN Presenca p ON p.jogo = j
    WHERE s.id_sala = :idSala
    GROUP BY j.id, j.nome, j.dataCriacao, s.id_sala
    ORDER BY j.dataCriacao DESC
""")
    List<JogoResumoDTO> listarResumoPorSala(@Param("idSala") Integer idSala);

    /* Resumo dos jogos vinculados a uma sala específica do professor informado */
    @Query("""
    SELECT new com.altf7.sei.dto.jogo.JogoResumoDTO(
        j.id,
        j.nome,
        j.dataCriacao,
        COUNT(p.aluno),
        s.id_sala
    )
    FROM Jogo j
    JOIN j.sala s
    LEFT JOIN Presenca p ON p.jogo = j
    WHERE s.id_sala = :idSala AND s.professor.id_professor = :idProfessor
    GROUP BY j.id, j.nome, j.dataCriacao, s.id_sala
    ORDER BY j.dataCriacao DESC
""")
    List<JogoResumoDTO> listarResumoPorSalaEProfessor(@Param("idSala") Integer idSala, @Param("idProfessor") Integer idProfessor);

    /* Verifica se o jogo informado está vinculado a uma sala do professor informado */
    @Query("""
    SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END
    FROM Jogo j
    WHERE j.id = :idJogo AND j.sala.professor.id_professor = :idProfessor
""")
    boolean pertenceAoProfessor(@Param("idJogo") Integer idJogo, @Param("idProfessor") Integer idProfessor);

    /* Lista os jogos vinculados a uma sala específica (uso direto, sem agregação de presenças) */
    @Query("SELECT j FROM Jogo j WHERE j.sala.id_sala = :idSala")
    List<Jogo> listarPorSala(@Param("idSala") Integer idSala);

    /* Conta quantos jogos existem vinculados a uma sala específica */
    @Query("SELECT COUNT(j) FROM Jogo j WHERE j.sala.id_sala = :idSala")
    long countBySala(@Param("idSala") Integer idSala);
}