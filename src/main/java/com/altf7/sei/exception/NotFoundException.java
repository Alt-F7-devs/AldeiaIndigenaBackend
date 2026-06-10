package com.altf7.sei.exception;

public class NotFoundException extends RuntimeException {
    /* NOT FOUND --> ALUNO */
    public static class AlunoNotFoundException extends RuntimeException {
        public AlunoNotFoundException() {
            super("Aluno não encontrado!");
        }
    }
    /* NOT FOUND --> SALA */
    public static class SalaNotFoundException extends RuntimeException {
        public SalaNotFoundException() {
            super("Sala não encontrada!");
        }
    }
}