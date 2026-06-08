package com.altf7.sei.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "aluno", schema = "sei_db")
@Getter
@Setter
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aluno_id")
    private Integer id_aluno;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cgm", nullable = false, unique = true)
    private Integer cgm;

    @Column(name = "senha", nullable = false, length = 100)
    private String senha;

    @OneToMany(mappedBy = "aluno")
    private List<Presenca> presencas;
}
