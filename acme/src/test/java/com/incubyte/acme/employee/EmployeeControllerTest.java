package com.incubyte.acme.employee;

import com.incubyte.acme.common.GlobalExceptionHandler;
import com.incubyte.acme.employee.dto.EmployeeCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void shouldReturnBadRequestWhenEmployeeRequestIsInvalid()
            throws Exception {

        String request = """
                {
                    "employeeCode": "",
                    "firstName": "",
                    "lastName": "Tanwar",
                    "email": "invalid-email",
                    "country": "India",
                    "department": "Engineering",
                    "jobTitle": "Software Engineer"
                }
                """;

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(employeeService);
    }
}