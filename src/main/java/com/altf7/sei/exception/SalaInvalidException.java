package com.altf7.sei.exception;

public class SalaInvalidException extends RuntimeException {

    public static class SalaNotFoundExceptionAll extends RuntimeException {
        public SalaNotFoundExceptionAll(){
            super("ERROR: Sala não foi encontrada!");
        }
    }

    public static class SalaNotFoundExceptionId extends RuntimeException {
        public SalaNotFoundExceptionId(Integer id_sala){
            super("ERROR: Sala não foi encontrada com o ID:" + id_sala);
        }
    }
}
