package com.altf7.sei.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_sala;

    @OneToMany(mappedBy = "sala")
    private List<Aluno> alunos = new ArrayList<>();

    private String num_sa;

    private LocalDate data;

    @PrePersist
    public void prePersist() {
        this.data = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
    }

    @ManyToOne
    @JoinColumn(name = "professor_id_professor")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "admin_login")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "jogo_id_jogo")
    private Jogo jogo;

    @OneToMany(mappedBy = "sala")
    private List<Presenca> presencas;

}
