package ru.javaops.restaurant_voting.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.javaops.restaurant_voting.error.AppException;
import ru.javaops.restaurant_voting.error.DataConflictException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail body = ex.updateAndGetBody(this.messageSource, LocaleContextHolder.getLocale());
        Map<String, String> invalidParams = new LinkedHashMap<>();
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            invalidParams.put(error.getObjectName(), getErrorMessage(error));
        }
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            invalidParams.put(error.getField(), getErrorMessage(error));
        }
        body.setProperty("invalid_params", invalidParams);
        body.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        return handleExceptionInternal(ex, body, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<?> dataConflictException(DataConflictException ex, WebRequest request) {
        log.error("DataConflictException: {}", ex.getMessage());
        return createProblemDetailExceptionResponse(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appException(AppException ex, WebRequest request) {
        log.error("ApplicationException: {}", ex.getMessage());
        return createProblemDetailExceptionResponse(ex, ex.getStatusCode(), request);
    }

    private ResponseEntity<?> createProblemDetailExceptionResponse(Exception ex, HttpStatusCode statusCode, WebRequest request) {
        ProblemDetail body = createProblemDetail(ex, statusCode, ex.getMessage(), null, null, request);
        return handleExceptionInternal(ex, body, new HttpHeaders(), statusCode, request);
    }

    private String getErrorMessage(ObjectError error) {
        return messageSource.getMessage(
                error.getCode(), error.getArguments(), error.getDefaultMessage(), LocaleContextHolder.getLocale());
    }
}
