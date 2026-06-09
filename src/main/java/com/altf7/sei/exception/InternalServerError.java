package com.altf7.sei.exception;

public class InternalServerError extends RuntimeException{

    /* INTERNAL SERVER ERROR --> SALA */

    public static class SalaInternalServerError extends RuntimeException {
        public SalaInternalServerError(){
            super("Erro interno do servidor! Não foi possível criar Sala!");
        }
    }

    public static class SalaAlunoInternalServerError extends RuntimeException {
        public SalaAlunoInternalServerError(){
            super("Erro interno do servidor! Não foi possível adicionar Aluno a Sala!");
        }
    }

    public static class SalaJogoInternalServerError extends RuntimeException {
        public SalaJogoInternalServerError(){
            super("Erro interno do servidor! Não foi possível adicionar Jogo a Sala!");
        }
    }

    public static class SalaListInternalServerError extends RuntimeException {
        public SalaListInternalServerError(){
            super("Erro interno do servidor! Não foi possível listar Sala(s)!");
        }
    }

    /* INTERNAL SERVER ERROR --> ADMIN */
    public static class AdminInternalServerError extends RuntimeException {
        public AdminInternalServerError(){
            super("Erro interno do servidor! Não foi possível criar Admin!");
        }
    }

    /* INTERNAL SERVER ERROR --> ALUNO */
    public static class AlunoInternalServerError extends RuntimeException {
        public AlunoInternalServerError(){
            super("Erro interno do servidor! Não foi possível criar Aluno!");
        }
    }

    public static class AlunoListInternalServerError extends RuntimeException {
        public AlunoListInternalServerError(){
            super("Erro interno do servidor! Não foi possível listar Aluno(s)!");
        }
    }

    /* INTERNAL SERVER ERROR --> PROFESSOR */
    public static class ProfessorInternalServerError extends RuntimeException {
        public ProfessorInternalServerError(){
            super("Erro interno do servidor! Não foi possível criar Professor!");
        }
    }

    public static class ProfessorListInternalServerError extends RuntimeException {
        public ProfessorListInternalServerError(){
            super("Erro interno do servidor! Não foi possível listar Professor(es)!");
        }
    }
}