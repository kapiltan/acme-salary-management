package com.incubyte.acme.salary.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalaryCreateRequest(

        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,

        @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String currency,

        @NotNull LocalDate effectiveFrom) {
}