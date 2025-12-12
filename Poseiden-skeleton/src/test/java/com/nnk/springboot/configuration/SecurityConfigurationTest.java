package com.nnk.springboot.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityConfiguration.class)
class SecurityConfigurationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should return ok for user with role USER")
    @WithMockUser(username = "user", roles = "USER")
    void publicEndpointShouldBeAccessibleWithoutAdminRole() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return Forbidden for user with role USER")
    @WithMockUser(username = "user", roles = "USER")
    void protectedEndpointShouldReturnUnauthorizedWithoutAdminRole() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Should return ok for user with role ADMIN")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void protectedEndpointShouldReturnUnauthorizedWithAdminAuth() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk());
    }
}