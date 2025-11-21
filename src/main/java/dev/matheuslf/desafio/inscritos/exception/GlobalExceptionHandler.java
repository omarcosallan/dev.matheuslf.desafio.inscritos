package dev.matheuslf.desafio.inscritos.exception;

import dev.matheuslf.desafio.inscritos.dto.error.ProblemDetail;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.AuthenticationException;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetail> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        ProblemDetail problem =
                new ProblemDetail(
                        "Erro de autenticação",
                        ex.getMessage(),
                        HttpStatus.UNAUTHORIZED.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(HttpServletRequest request) {
        ProblemDetail problem =
                new ProblemDetail(
                        "Erro de autenticação",
                        "E-mail ou senha inválidos",
                        HttpStatus.UNAUTHORIZED.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest req) {
        ProblemDetail problem = new ProblemDetail(
                "Recurso não encontrado",
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                getRequestPath(req)
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ProblemDetail> handleConflictException(ConflictException e, HttpServletRequest req) {
        ProblemDetail problem = new ProblemDetail(
                "Erro de conflito",
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                getRequestPath(req)
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(InvalidDueDateException.class)
    public ResponseEntity<ProblemDetail> handleInvalidDueDateException(InvalidDueDateException e, HttpServletRequest req) {
        ProblemDetail problem = new ProblemDetail(
                "Data inválida",
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                getRequestPath(req)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(ProjectEndedException.class)
    public ResponseEntity<ProblemDetail> handleProjectEndedException(ProjectEndedException e, HttpServletRequest req) {
        ProblemDetail problem = new ProblemDetail(
                "Projeto finalizado",
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                getRequestPath(req)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest req) {
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream().collect(
                Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existingMessage, newMessage) -> existingMessage + ", " + newMessage)
        );

        ProblemDetail problem = new ProblemDetail(
                "Dados inválidos",
                "Um ou mais campos são inválidos",
                HttpStatus.BAD_REQUEST.value(),
                getRequestPath(req));

        problem.setProperty("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String detail =
                String.format(
                        "O parâmetro '%s' possui o valor inválido '%s'. Tipo esperado: %s",
                        e.getName(),
                        e.getValue(),
                        e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "desconhecido");

        ProblemDetail problem =
                new ProblemDetail(
                        "Parâmetro inválido",
                        detail,
                        HttpStatus.BAD_REQUEST.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        ProblemDetail problem =
                new ProblemDetail(
                        "Solicitação JSON malformada",
                        "O corpo da solicitação é inválido ou está malformado",
                        HttpStatus.BAD_REQUEST.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(HttpServletRequest request) {
        ProblemDetail problem =
                new ProblemDetail(
                        "Erro interno do servidor",
                        "Ocorreu um erro inesperado",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    private String getRequestPath(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
