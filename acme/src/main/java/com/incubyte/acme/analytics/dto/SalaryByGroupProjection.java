package com.incubyte.acme.analytics.dto;

import java.math.BigDecimal;

public interface SalaryByGroupProjection {

    String getGroupName();

    Long getEmployeeCount();

    BigDecimal getAverageSalary();
}