package com.altf7.sei.exception;

public class AccessDeniedCustomException extends RuntimeException {
    public AccessDeniedCustomException() {
        super("ERROR: Você não tem permissão para acessar este recurso.");
    }
}