package ru.ddc.consultationsservice.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations()
                .forEach(constraintViolation -> {
                    String fieldName = "";
                    for (Path.Node node : constraintViolation.getPropertyPath()) {
                        fieldName = node.getName() == null || node.getName().startsWith("<") ? fieldName : node.getName();
                    }
                    String errorMessage = constraintViolation.getMessage();
                    errors.put(fieldName, errorMessage);
                });
        return ResponseEntity.badRequest().body(errors);
    }
}
