package com.altf7.sei.repository;

import com.altf7.sei.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Integer> {

    @Query("SELECT a FROM Aluno a WHERE a.sala.id_sala = :id_sala")
    List<Aluno> findBySalaId(@Param("id_sala") Integer id_sala);

    Optional<Aluno> findByCgm(String cgm);

    @Query("SELECT COUNT(a) > 0 FROM Aluno a WHERE a.sala.id_sala = :id_sala AND a.id_aluno = :id_aluno")
    boolean existsAlunoNaSala(@Param("id_sala") Integer id_sala, @Param("id_aluno") Integer id_aluno);

    @Query("SELECT COUNT(a) FROM Aluno a WHERE a.id_aluno = :idAluno AND a.sala IS NOT NULL")
    long countSalasByAluno(@Param("idAluno") Integer idAluno);
}