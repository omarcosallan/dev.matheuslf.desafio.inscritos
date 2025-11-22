package dev.matheuslf.desafio.inscritos.exception.handler;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.matheuslf.desafio.inscritos.dto.error.ProblemDetail;
import dev.matheuslf.desafio.inscritos.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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
    public ResponseEntity<ProblemDetail> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        ProblemDetail problem =
                new ProblemDetail(
                        "Authentication error",
                        e.getMessage(),
                        HttpStatus.UNAUTHORIZED.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler({
            BadCredentialsException.class,
            InternalAuthenticationServiceException.class
    })
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(HttpServletRequest request) {
        ProblemDetail problem =
                new ProblemDetail(
                        "Authentication failed",
                        "Invalid email or password",
                        HttpStatus.UNAUTHORIZED.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest req) {
        ProblemDetail problem = new ProblemDetail(
                "Resource not found",
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                getRequestPath(req)
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ProblemDetail> handleConflictException(ConflictException e, HttpServletRequest req) {
        ProblemDetail problem = new ProblemDetail(
                "Conflict error",
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                getRequestPath(req)
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ProblemDetail> handleInvalidDueDateException(InvalidDateException e, HttpServletRequest req) {
        ProblemDetail problem = new ProblemDetail(
                "Invalid date",
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                getRequestPath(req)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(BusinessException e, HttpServletRequest req) {
        ProblemDetail problem = new ProblemDetail(
                "Business rule violation",
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
                "Validation error",
                "One or more fields are invalid",
                HttpStatus.BAD_REQUEST.value(),
                getRequestPath(req));

        problem.setProperty("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String detail =
                String.format(
                        "Parameter '%s' has invalid value '%s'. Expected type: %s",
                        e.getName(),
                        e.getValue(),
                        e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown");

        ProblemDetail problem =
                new ProblemDetail(
                        "Invalid parameter",
                        detail,
                        HttpStatus.BAD_REQUEST.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        ProblemDetail problem =
                new ProblemDetail(
                        "Malformed JSON request",
                        "Request body is invalid or malformed: " + e.getCause().getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler({
            JWTCreationException.class,
            JWTVerificationException.class,
            JWTDecodeException.class,
    })
    public ResponseEntity<ProblemDetail> handleJWTException(Exception e, HttpServletRequest request) {
        ProblemDetail problem =
                new ProblemDetail(
                        "Invalid token",
                        e.getMessage(),
                        HttpStatus.FORBIDDEN.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(HttpServletRequest request) {
        ProblemDetail problem =
                new ProblemDetail(
                        "Internal server error",
                        "An unexpected error occurred",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    private String getRequestPath(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
