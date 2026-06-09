package com.altf7.sei.repository;

import com.altf7.sei.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Integer> {
    @Query("SELECT a FROM Aluno a WHERE a.sala.id_sala = :id_sala")
    List<Aluno> findBySala_IdSala(Integer id_sala);
    Optional<Aluno> findByCgm(String cgm);
}