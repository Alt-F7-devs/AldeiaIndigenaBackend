package com.altf7.sei.entity;

import jakarta.persistence.*;

@Entity
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jogo_id")
    private Integer id;

    private String nome;
}