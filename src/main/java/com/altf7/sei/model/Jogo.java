package com.altf7.sei.model;

import jakarta.persistence.*;

@Entity
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_jogo;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
}