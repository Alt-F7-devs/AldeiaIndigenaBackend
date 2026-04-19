package com.altf7.sei.model;

import jakarta.persistence.*;

@Entity
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_professor;

    private String nome;
    private String materia;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
}
