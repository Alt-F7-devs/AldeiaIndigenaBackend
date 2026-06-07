package com.altf7.sei.exception;

public class ProfessorInvalidException extends RuntimeException{

    public static class ProfessorNotFoundExceptionAll extends RuntimeException {
        public ProfessorNotFoundExceptionAll(){
            super("ERROR: Professor não foi encontrado!");
        }
    }

    public static class ProfessorNotFoundExceptionId extends RuntimeException {
        public ProfessorNotFoundExceptionId(Integer id_professor){
            super("ERROR: Professor não foi encontrado com o ID:" + id_professor);
        }
    }

    public static class ProfessorComSalasVinculadasException extends RuntimeException {
        public ProfessorComSalasVinculadasException(){
            super("ERROR: Professor possui salas vinculadas. Desvincule as salas antes de excluir.");
        }
    }
}
