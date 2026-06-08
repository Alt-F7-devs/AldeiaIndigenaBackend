package com.altf7.sei.exception;

public class AdminInvalidException extends RuntimeException{

    public static class AdminNotFoundExceptionAll extends RuntimeException {
        public AdminNotFoundExceptionAll(){
            super("ERROR: Admin não foi encontrado!");
        }
    }
}
