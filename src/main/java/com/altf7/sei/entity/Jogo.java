package com.altf7.sei.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jogo")
    private Integer id;

    private String nome;

    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    @ManyToOne
    @JoinColumn(name = "admin_login")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "sala_id_sala")
    private Sala sala;

    @OneToMany(mappedBy = "jogo")
    private List<Presenca> presencas = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
    }
}