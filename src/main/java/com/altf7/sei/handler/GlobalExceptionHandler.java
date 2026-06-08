package com.altf7.sei.handler;

import com.altf7.sei.dto.error.ErrorResponse;
import com.altf7.sei.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* Exception --> CGM Inválido */
    @ExceptionHandler(CgmInvalidException.class)
    public ResponseEntity<ErrorResponse> cgmInvalid(CgmInvalidException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /* Exception --> CPF Inválido */
    @ExceptionHandler(CpfInvalidException.class)
    public ResponseEntity<ErrorResponse> cpfInvalid(CpfInvalidException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /* Exception --> Password Invalid */
    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorResponse> passwordInvalid(PasswordInvalidException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /* Exception --> Error 500 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse error = new ErrorResponse("Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

   /* Internal Server Error 500 */
   @ExceptionHandler(InternalServerError.class)
   public ResponseEntity<ErrorResponse> handleInternalErrors(InternalServerError ex) {
       ErrorResponse error = new ErrorResponse(ex.getMessage(), 500);  // ← getMessage() = "Não foi possível criar Sala!"
       return ResponseEntity.status(500).body(error);
   }

    /* Exception --> NotFound Jogo ID*/
    @ExceptionHandler(JogoInvalidException.JogoNotFoundException.class)
    public ResponseEntity<ErrorResponse> jogoNotFound(JogoInvalidException.JogoNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /* Exception --> NotFound Jogo Geral*/
    @ExceptionHandler(JogoInvalidException.JogoNotFoundExceptionAll.class)
    public ResponseEntity<ErrorResponse> jogoNotFundAll(JogoInvalidException.JogoNotFoundExceptionAll ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /* Exception --> NotFound Admin Geral*/
    @ExceptionHandler(AdminInvalidException.AdminNotFoundExceptionAll.class)
    public ResponseEntity<ErrorResponse> AdminNotFoundAll(AdminInvalidException.AdminNotFoundExceptionAll ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /* Exception --> NotFound Professor Geral*/
    @ExceptionHandler(ProfessorInvalidException.ProfessorNotFoundExceptionAll.class)
    public ResponseEntity<ErrorResponse> professorNotFoundAll(ProfessorInvalidException.ProfessorNotFoundExceptionAll ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /* Exception --> NotFound Professor ID*/
    @ExceptionHandler(ProfessorInvalidException.ProfessorNotFoundExceptionId.class)
    public ResponseEntity<ErrorResponse> professorNotFoundId(ProfessorInvalidException.ProfessorNotFoundExceptionId ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /* Exception --> Professor com Salas Vinculadas (bloqueia exclusão) */
    @ExceptionHandler(ProfessorInvalidException.ProfessorComSalasVinculadasException.class)
    public ResponseEntity<ErrorResponse> professorComSalasVinculadas(ProfessorInvalidException.ProfessorComSalasVinculadasException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /* Exception --> NotFound Aluno Geral*/
    @ExceptionHandler(AlunoInvalidException.AlunoNotFoundExceptionAll.class)
    public ResponseEntity<ErrorResponse> alunoNotFoundAll(AlunoInvalidException.AlunoNotFoundExceptionAll ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /* Exception --> NotFound Aluno ID*/
    @ExceptionHandler(AlunoInvalidException.AlunoNotFoundExceptionId.class)
    public ResponseEntity<ErrorResponse> alunoNotFoundId(AlunoInvalidException.AlunoNotFoundExceptionId ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /* Exception --> NotFound Sala Geral*/
    @ExceptionHandler(SalaInvalidException.SalaNotFoundExceptionAll.class)
    public ResponseEntity<ErrorResponse> salaNotFoundAll(SalaInvalidException.SalaNotFoundExceptionAll ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /* Exception --> NotFound Sala ID*/
    @ExceptionHandler(SalaInvalidException.SalaNotFoundExceptionId.class)
    public ResponseEntity<ErrorResponse> salaNotFoundId(SalaInvalidException.SalaNotFoundExceptionId ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
