package com.altf7.sei.exception;

public class AlunoInvalidException extends RuntimeException{

    public static class AlunoNotFoundExceptionAll extends RuntimeException {
        public AlunoNotFoundExceptionAll(){
            super("ERROR: Aluno não foi encontrado!");
        }
    }

    public static class AlunoNotFoundExceptionId extends RuntimeException {
        public AlunoNotFoundExceptionId(Integer id_aluno){
            super("ERROR: Aluno não foi encontrado com o ID:" + id_aluno);
        }
    }
}
