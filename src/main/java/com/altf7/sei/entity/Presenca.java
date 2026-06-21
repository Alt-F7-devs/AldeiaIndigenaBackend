package com.altf7.sei.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


//essa constraint é para garantir que a combinação de aluno e jogo não se repita
@Entity
@Table(
        name = "presenca",
        schema = "sei_db",
        uniqueConstraints = @UniqueConstraint(columnNames = {"aluno_id", "jogo_id"})
)
@Getter
@Setter
public class Presenca {

    @EmbeddedId
    private PresencaId id = new PresencaId();

    @ManyToOne
    @MapsId("aluno")
    @JoinColumn(name = "aluno_id_aluno")
    private Aluno aluno;

    @ManyToOne
    @MapsId("jogo")
    @JoinColumn(name = "jogo_id_jogo")
    private Jogo jogo;
}