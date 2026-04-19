package com.altf7.sei.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_aluno;

    private String nome;
    private String senha;
    private Integer cgm;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "aluno")
    private List<Presenca> presencas;
}