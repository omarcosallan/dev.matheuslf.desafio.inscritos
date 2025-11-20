package dev.matheuslf.desafio.inscritos.exception;

import dev.matheuslf.desafio.inscritos.dto.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest req) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = toErrorResponse(e.getMessage(), req, status);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest req) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        Map<String, String> errors = getErrorsDetails(e);
        ErrorResponse response = toErrorResponse("Dados inválidos", req, status);
        response.setProperty("errors", errors);
        return new ResponseEntity<>(response, status);
    }

    private ErrorResponse toErrorResponse(String message, WebRequest req, HttpStatus status) {
        return new ErrorResponse(
                LocalDateTime.now(),
                message,
                req.getDescription(false).replace("uri=", ""),
                status,
                status.value()
        );
    }

    private Map<String, String> getErrorsDetails(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existingMessage, newMessage) -> existingMessage + ", " + newMessage)
                );
    }
}
