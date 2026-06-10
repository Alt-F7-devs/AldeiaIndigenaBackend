package com.altf7.sei.exception;

public class ConflictException extends RuntimeException {
    /* CONFLICT --> PRESENCA */
    public static class PresencaJaRegistradaException extends RuntimeException {
        public PresencaJaRegistradaException() {
            super("Presença já registrada para este aluno nesta sala!");
        }
    }
}
