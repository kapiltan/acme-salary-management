package com.incubyte.acme.salary;

import com.incubyte.acme.common.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SalaryController.class)
@Import(GlobalExceptionHandler.class)
class SalaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SalaryService salaryService;

    @Test
    void shouldReturnBadRequestWhenSalaryRequestIsInvalid()
            throws Exception {

        String request = """
                {
                  "amount": 0,
                  "currency": "invalid",
                  "effectiveFrom": null
                }
                """;

        mockMvc.perform(post("/api/employees/1/salary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.amount").exists())
                .andExpect(jsonPath("$.errors.currency").exists())
                .andExpect(jsonPath("$.errors.effectiveFrom").exists());

        verifyNoInteractions(salaryService);
    }
}