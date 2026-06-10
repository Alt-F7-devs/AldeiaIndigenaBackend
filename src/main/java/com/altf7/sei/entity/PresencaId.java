package com.altf7.sei.entity;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PresencaId implements Serializable {

    private Integer aluno;
    private Integer sala;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PresencaId)) return false;
        PresencaId that = (PresencaId) o;
        return Objects.equals(aluno, that.aluno) && Objects.equals(sala, that.sala);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aluno, sala);
    }
}