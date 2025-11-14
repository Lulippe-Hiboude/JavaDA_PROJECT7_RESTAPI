package com.nnk.springboot.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityConfiguration.class)
class CustomAuthenticationSuccessHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        final UserDetails user = org.springframework.security.core.userdetails.User
                .withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        final UserDetails admin = org.springframework.security.core.userdetails.User
                .withUsername("admin")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build();

        given(userDetailsService.loadUserByUsername("user")).willReturn(user);
        given(userDetailsService.loadUserByUsername("admin")).willReturn(admin);
    }

    @Test
    @DisplayName("Login as a User should redirect to home")
    void loginAsAUserShouldRedirectToHome() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "user")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @DisplayName("Login as a Admin should redirect to admin home")
    void loginAsAdminShouldRedirectToAdminHome() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "admin")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/home"));
    }

    @Test
    @DisplayName("accessing protected endpoint without login should redirect to login form")
    void accessProtectedEndpointWithoutLoginShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("a User should not access admin home")
    @WithMockUser(username = "user", roles = "USER")
    void userCannotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/admin/home"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("an Admin should access admin home which is bidList")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void adminCanAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk());
    }
}