package com.pm.patientservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String,String>> handleValidation(MethodArgumentNotValidException ex){
        Map<String,String> errors=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error->errors.put(error.getField(),error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(value = {EmailAlreadyExistsException.class})
    public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){
        log.warn(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(value={PatientNotFoundException.class})
    public ResponseEntity<Map<String,String>> handlePatientNotFoundException(PatientNotFoundException ex){
        log.warn(ex.getMessage());
        Map<String,String> errors=new HashMap<>();
        errors.put("message",ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}
