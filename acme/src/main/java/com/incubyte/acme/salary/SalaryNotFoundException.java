package com.incubyte.acme.salary;

public class SalaryNotFoundException extends RuntimeException {

    public SalaryNotFoundException(String message) {
        super(message);
    }
}