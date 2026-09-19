package com.incubyte.acme.common;

import com.incubyte.acme.employee.EmployeeAlreadyExistsException;
import com.incubyte.acme.employee.EmployeeNotFoundException;
import com.incubyte.acme.salary.SalaryAlreadyExistsException;
import com.incubyte.acme.salary.SalaryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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