package com.altf7.sei.repository;

import com.altf7.sei.entity.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PresencaRepository extends JpaRepository<Presenca, Integer> {

    @Query("SELECT COUNT(p) > 0 FROM Presenca p WHERE p.aluno.id_aluno = :idAluno AND p.sala.id_sala = :idSala")
    boolean existsByAlunoESala(@Param("idAluno") Integer idAluno, @Param("idSala") Integer idSala);

    @Query("SELECT COUNT(p) FROM Presenca p WHERE p.aluno.id_aluno = :idAluno")
    long countByAluno(@Param("idAluno") Integer idAluno);
}
