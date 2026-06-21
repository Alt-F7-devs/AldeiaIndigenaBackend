package com.altf7.sei.repository;

import com.altf7.sei.entity.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PresencaRepository extends JpaRepository<Presenca, Integer> {

    @Query("SELECT COUNT(p) FROM Presenca p WHERE p.jogo.id = :idJogo")
    long countByJogoId(@Param("idJogo") Integer idJogo);

    @Query("SELECT COUNT(p) > 0 FROM Presenca p WHERE p.aluno.id_aluno = :idAluno AND p.jogo.id = :idJogo")
    boolean existsByAlunoEJogo(@Param("idAluno") Integer idAluno, @Param("idJogo") Integer idJogo);

    @Query("SELECT COUNT(p) FROM Presenca p WHERE p.aluno.id_aluno = :idAluno")
    long countByAluno(@Param("idAluno") Integer idAluno);

    /* Conta quantos jogos de uma sala o aluno tem presença registrada */
    @Query("SELECT COUNT(p) FROM Presenca p WHERE p.aluno.id_aluno = :idAluno AND p.jogo.sala.id_sala = :idSala")
    long countByAlunoESala(@Param("idAluno") Integer idAluno, @Param("idSala") Integer idSala);

    /* Lista as presenças de um aluno em uma sala (para saber quais jogos específicos) */
    @Query("SELECT p FROM Presenca p WHERE p.aluno.id_aluno = :idAluno AND p.jogo.sala.id_sala = :idSala")
    List<Presenca> listarPorAlunoESala(@Param("idAluno") Integer idAluno, @Param("idSala") Integer idSala);

    /* Lista os IDs dos alunos com presença registrada em um jogo específico */
    @Query("SELECT p.aluno.id_aluno FROM Presenca p WHERE p.jogo.id = :idJogo")
    List<Integer> listarIdsAlunosPresentesNoJogo(@Param("idJogo") Integer idJogo);

    @Modifying
    @Query("DELETE FROM Presenca p WHERE p.aluno.id_aluno = :idAluno AND p.jogo.id = :idJogo")
    void deleteByAlunoEJogo(@Param("idAluno") Integer idAluno, @Param("idJogo") Integer idJogo);
}