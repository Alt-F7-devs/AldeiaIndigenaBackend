package com.altf7.sei.exception;

/* Exception Message --> CPF Inválido */
public class CpfInvalidException extends RuntimeException {
    public CpfInvalidException(String message) {
        super(message);
    }
}
