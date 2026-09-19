package com.incubyte.acme.salary;

public class SalaryAlreadyExistsException extends RuntimeException {

    public SalaryAlreadyExistsException(String message) {
        super(message);
    }
}