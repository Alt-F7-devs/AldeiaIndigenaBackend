package com.altf7.sei.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_sala;

    private String numSa;
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "jogo_id")
    private Jogo jogo;

    @OneToMany(mappedBy = "sala")
    private List<Presenca> presencas;
}
