package com.altf7.sei.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


//essa constraint é para garantir que a combinação de aluno e sala não se repita
@Entity
@Table(
    name = "presenca",
    schema = "sei_db",
    uniqueConstraints = @UniqueConstraint(columnNames = {"aluno_id", "sala_id"})
)
@Getter
@Setter
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "presenca_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "sala_id")
    private Sala sala;
}
