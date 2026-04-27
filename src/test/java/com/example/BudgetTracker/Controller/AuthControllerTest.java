package com.example.BudgetTracker.Controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.example.BudgetTracker.DTO.UserRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_ValidUser_Returns200() throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("validemail@gmail.com");
        dto.setPassword("123456");
        dto.setFullName("ValidEmail Test");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("integrationtest@gmail.com"))
                .andExpect(jsonPath("$.fullName").value("Integration Test"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void register_DuplicateEmail_Returns400() throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("duplicateemail@gmail.com");
        dto.setPassword("123456");
        dto.setFullName("DuplicateEmail Test");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.message").value("Email is already in use"));
    }

    @Test
    void login_ValidUser_Returns200() throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("logintest@gmail.com");
        dto.setPassword("123456");
        dto.setFullName("Login Test");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/login")
                        .param("email", "logintest@gmail.com")
                        .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.emptyString())));
    }

    @Test
    void login_InvalidPassword_Returns400() throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("logintest2@gmail.com");
        dto.setPassword("123456");
        dto.setFullName("Login Test");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // login with wrong password
        mockMvc.perform(post("/api/auth/login")
                        .param("email", "logintest2@gmail.com")
                        .param("password", "wrongpassword"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid password"));
    }

    @Test
    void login_EmailNotFound_Returns400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .param("email", "nonexistent@gmail.com")
                        .param("password", "123456"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

}