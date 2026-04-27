package com.example.BudgetTracker.Controller;


import com.example.BudgetTracker.DTO.AccountRequestDTO;
import com.example.BudgetTracker.DTO.UserRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String getToken(String email) throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail(email);
        dto.setPassword("123456");
        dto.setFullName("Test User");

        // register
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // login and extract token
        return mockMvc.perform(post("/api/auth/login")
                        .param("email", email)
                        .param("password", "123456"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void createAccount_ValidToken_Returns200() throws Exception {
        String token = getToken("accounttest@gmail.com");

        AccountRequestDTO dto = new AccountRequestDTO();
        dto.setType("Checking");
        dto.setName("My Account");
        dto.setBalance(BigDecimal.valueOf(1000));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My Account"))
                .andExpect(jsonPath("$.type").value("Checking"))
                .andExpect(jsonPath("$.balance").value(1000.0));
    }

    @Test
    void createAccount_NoToken_Returns403() throws Exception {
        AccountRequestDTO dto = new AccountRequestDTO();
        dto.setType("Checking");
        dto.setName("My Account");
        dto.setBalance(BigDecimal.valueOf(1000));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }
}