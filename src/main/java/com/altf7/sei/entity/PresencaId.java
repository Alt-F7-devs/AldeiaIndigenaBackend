package com.altf7.sei.entity;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PresencaId implements Serializable {

    private Integer aluno;
    private Integer jogo;

    public Integer getAluno() {
        return aluno;
    }

    public void setAluno(Integer aluno) {
        this.aluno = aluno;
    }

    public Integer getJogo() {
        return jogo;
    }

    public void setJogo(Integer jogo) {
        this.jogo = jogo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PresencaId)) return false;
        PresencaId that = (PresencaId) o;
        return Objects.equals(aluno, that.aluno) && Objects.equals(jogo, that.jogo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aluno, jogo);
    }
}