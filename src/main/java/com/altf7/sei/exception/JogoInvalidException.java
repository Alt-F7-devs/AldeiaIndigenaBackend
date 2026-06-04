package com.altf7.sei.exception;

public class JogoInvalidException extends RuntimeException {

    public JogoInvalidException(String message) {super(message);}

    public static class JogoNotFoundException extends RuntimeException {
        public JogoNotFoundException(Integer id) {
            super("Jogo não encontrado com id: " + id);
        }
    }
}