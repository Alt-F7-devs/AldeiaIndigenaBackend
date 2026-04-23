package com.altf7.sei.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "professor", schema = "sei_db")
@Getter
@Setter
public class Professor {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "professor_id")
        private Integer id_professor;

        @Column(name = "nome", nullable = false)
        private String nome;

        @Column(name = "materia", nullable = false)
        private String materia;

        @Column(name = "cpf", nullable = false, unique = true)
        private String cpf; // login

        @Column(name = "senha", nullable = false, length = 100)
        private String senha;
}
