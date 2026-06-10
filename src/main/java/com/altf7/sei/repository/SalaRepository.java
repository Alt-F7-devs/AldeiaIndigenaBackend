package com.altf7.sei.repository;

import com.altf7.sei.entity.Professor;
import com.altf7.sei.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Integer> {

    List<Sala> findByProfessor(Professor professor);

    @Query("SELECT s FROM Sala s WHERE s.professor.id_professor = :idProfessor")
    List<Sala> findByProfessorId(@Param("idProfessor") Integer idProfessor);
}
