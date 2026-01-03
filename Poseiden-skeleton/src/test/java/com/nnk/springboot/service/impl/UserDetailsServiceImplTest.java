package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    @DisplayName("should load user by username")
    void shouldLoadUserByUsername() {
        //given
        final String username = "username";
        final String fullname = "fullname";
        final String role = "ROLE_USER";
        final String hashedPassword = "hashedPassword";
        final User user = User.builder()
                .username(username)
                .fullname(fullname)
                .role(role)
                .password(hashedPassword)
                .build();
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        //when
        final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        //then
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(hashedPassword, userDetails.getPassword());
        assertNotNull(userDetails.getAuthorities());
    }

    @Test
    @DisplayName("should throw UsernameNotFoundException")
    void shouldThrowUsernameNotFoundException() {
        //given
        final String username = "username";
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        //when & then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername(username));

    }

}