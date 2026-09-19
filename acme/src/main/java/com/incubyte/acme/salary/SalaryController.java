package com.incubyte.acme.salary;

import com.incubyte.acme.salary.dto.SalaryCreateRequest;
import com.incubyte.acme.salary.dto.SalaryResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees/{employeeId}/salary")
public class SalaryController {

    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @GetMapping
    public SalaryResponse getCurrentSalary(
            @PathVariable Long employeeId) {
        return SalaryResponse.from(
                salaryService.getCurrentSalary(employeeId));
    }

    @GetMapping("/history")
    public List<SalaryResponse> getSalaryHistory(
            @PathVariable Long employeeId) {
        return salaryService.getSalaryHistory(employeeId)
                .stream()
                .map(SalaryResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SalaryResponse addSalary(
            @PathVariable Long employeeId,
            @Valid @RequestBody SalaryCreateRequest request) {
        SalaryHistory salary = new SalaryHistory();

        salary.setAmount(request.amount());
        salary.setCurrency(request.currency());
        salary.setEffectiveFrom(request.effectiveFrom());

        return SalaryResponse.from(
                salaryService.addSalary(employeeId, salary));
    }
}