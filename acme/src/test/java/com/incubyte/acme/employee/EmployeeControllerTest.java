package com.incubyte.acme.employee;

import com.incubyte.acme.common.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void shouldReturnBadRequestWhenEmployeeRequestIsInvalid() throws Exception {
        String request = """
                {
                  "employeeCode": "",
                  "firstName": "",
                  "lastName": "",
                  "email": "invalid-email",
                  "country": "",
                  "department": "",
                  "jobTitle": ""
                }
                """;

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.employeeCode").exists())
                .andExpect(jsonPath("$.errors.email").exists());

        verifyNoInteractions(employeeService);
    }

    @Test
    void shouldReturnInternalServerErrorWhenUnexpectedExceptionOccurs() throws Exception {
        when(employeeService.getEmployee(1L))
                .thenThrow(new RuntimeException("database failure"));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}