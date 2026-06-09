package com.altf7.sei.exception;

public class JogoInvalidException extends RuntimeException {
    public JogoInvalidException(String message) {super(message);}

    public static class JogoNotFoundException extends RuntimeException {
        public JogoNotFoundException(Integer id) {
            super("ERROR: Jogo não encontrado com id: " + id);
        }
    }

    public static class JogoNotFoundExceptionAll extends RuntimeException {
        public JogoNotFoundExceptionAll() {
            super("ERROR: Jogo não foi encontrado!");
        }
    }
}