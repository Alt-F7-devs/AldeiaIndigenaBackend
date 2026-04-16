package com.altf7.sei.model;

import jakarta.persistence.*;


//essa constraint é para garantir que a combinação de aluno e sala não se repita
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"aluno_id", "sala_id"})
)
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "sala_id")
    private Sala sala;
}
