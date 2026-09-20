package com.incubyte.acme.common;

import com.incubyte.acme.employee.EmployeeAlreadyExistsException;
import com.incubyte.acme.employee.EmployeeNotFoundException;
import com.incubyte.acme.salary.SalaryAlreadyExistsException;
import com.incubyte.acme.salary.SalaryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(EmployeeNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public Map<String, Object> handleEmployeeNotFound(
                        EmployeeNotFoundException exception) {
                return error(
                                HttpStatus.NOT_FOUND,
                                exception.getMessage());
        }

        @ExceptionHandler(SalaryNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public Map<String, Object> handleSalaryNotFound(
                        SalaryNotFoundException exception) {
                return error(
                                HttpStatus.NOT_FOUND,
                                exception.getMessage());
        }

        @ExceptionHandler({
                        EmployeeAlreadyExistsException.class,
                        SalaryAlreadyExistsException.class
        })
        @ResponseStatus(HttpStatus.CONFLICT)
        public Map<String, Object> handleConflict(
                        RuntimeException exception) {
                return error(
                                HttpStatus.CONFLICT,
                                exception.getMessage());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Map<String, Object> handleValidation(
                        MethodArgumentNotValidException exception) {

                Map<String, String> validationErrors = new HashMap<>();

                exception.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> validationErrors.put(
                                                error.getField(),
                                                error.getDefaultMessage()));

                Map<String, Object> response = new HashMap<>();
                response.put("timestamp", Instant.now());
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.put("message", "Validation failed");
                response.put("errors", validationErrors);

                return response;
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public Map<String, Object> handleUnexpectedException(Exception exception) {
                return error(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "An unexpected error occurred");
        }

        private Map<String, Object> error(
                        HttpStatus status,
                        String message) {
                return Map.of(
                                "timestamp", Instant.now(),
                                "status", status.value(),
                                "error", status.getReasonPhrase(),
                                "message", message);
        }
}