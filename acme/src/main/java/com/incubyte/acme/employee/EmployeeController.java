package com.incubyte.acme.employee;

import com.incubyte.acme.employee.dto.EmployeeCreateRequest;
import com.incubyte.acme.employee.dto.EmployeeResponse;
import com.incubyte.acme.employee.dto.EmployeeUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public PageResponse<EmployeeResponse> searchEmployees(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String department,
            @PageableDefault(size = 20, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<EmployeeResponse> page = employeeService
                .searchEmployees(search, country, department, pageable)
                .map(EmployeeResponse::from);

        return PageResponse.from(page);
    }

    @GetMapping("/{id}")
    public EmployeeResponse getEmployee(@PathVariable Long id) {
        return EmployeeResponse.from(
                employeeService.getEmployee(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse createEmployee(
            @Valid @RequestBody EmployeeCreateRequest request) {
        Employee employee = employeeService.createEmployee(
                request.employeeCode(),
                request.firstName(),
                request.lastName(),
                request.email(),
                request.country(),
                request.department(),
                request.jobTitle());

        return EmployeeResponse.from(employee);
    }

    @PutMapping("/{id}")
    public EmployeeResponse updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeUpdateRequest request) {
        Employee employee = employeeService.updateEmployee(
                id,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.country(),
                request.department(),
                request.jobTitle());

        return EmployeeResponse.from(employee);
    }
}