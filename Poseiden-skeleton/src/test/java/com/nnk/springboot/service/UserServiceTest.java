package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.user.UserCreateDto;
import com.nnk.springboot.dto.user.UserDto;
import com.nnk.springboot.dto.user.UserUpdateDto;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.impl.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    @DisplayName("Should create a new user")
    void shouldCreateNewUser() {
        //given
        final String role = "USER";
        final String fullname = "fullname";
        final String password = "password";
        final String username = "username";
        final String hashedPassword = "hashedPassword";
        final UserCreateDto userCreateDto = UserCreateDto.builder()
                .username(username)
                .fullname(fullname)
                .password(password)
                .role(role)
                .build();
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());
        given(passwordEncoder.encode(password)).willReturn(hashedPassword);
        //when
        userService.handleEntityCreation(userCreateDto);

        //then
        verify(userRepository, (times(1))).save(userCaptor.capture());
        final User savedUser = userCaptor.getValue();
        assertEquals(username, savedUser.getUsername());
        assertEquals(fullname, savedUser.getFullname());
        assertEquals(hashedPassword, savedUser.getPassword());
        assertEquals(role, savedUser.getRole());
    }

    @Test
    @DisplayName("Should throw EntityExistsException if username already taken")
    void shouldThrowEntityExistsException() {
        //given
        final String role = "USER";
        final String fullname = "fullname";
        final String password = "password";
        final String username = "username";
        final String hashedPassword = "hashedPassword";
        final UserCreateDto userCreateDto = UserCreateDto.builder()
                .username(username)
                .fullname(fullname)
                .password(password)
                .role(role)
                .build();
        final User existingUser = User.builder()
                .username(username)
                .fullname(fullname)
                .password(hashedPassword)
                .role(role)
                .build();
        given(userRepository.findByUsername(username)).willReturn(Optional.of(existingUser));
        //when
        assertThrows(EntityExistsException.class, () -> userService.handleEntityCreation(userCreateDto));

        //then
        verify(passwordEncoder, (times(0))).encode(any());
        verify(userRepository, (times(0))).save(any());
    }

    @Test
    @DisplayName("should delete existing user by id")
    void shouldDeleteExistingUserById() {
        //given
        final Integer userId = 1;
        final String role = "USER";
        final String fullname = "fullname";
        final String hashedPassword = "hashedPassword";
        final String username = "username";
        final User existingUser = User.builder()
                .id(userId)
                .username(username)
                .fullname(fullname)
                .password(hashedPassword)
                .role(role)
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));

        //when
        userService.handleEntityDeletion(userId);
        //then
        verify(userRepository, (times(1))).deleteById(userId);
    }

    @Test
    @DisplayName("should throw EntityNotFoundException")
    void shouldThrowEntityNotFoundException() {
        //given
        final Integer userId = 1;

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //when & then
        assertThrows(EntityNotFoundException.class, () -> userService.handleEntityDeletion(userId));
        verify(userRepository, (times(0))).deleteById(userId);
    }

    @Test
    @DisplayName("should return a list of UserDto")
    void shouldReturnAListOfUserDto() {
        //given
        final int id = 1;
        final int id2 = 2;
        final String role = "USER";
        final String fullname = "fullname";
        final String username = "username";
        final String username2 = "username2";
        final String hashedPassword = "hashedPassword";
        final User user = User.builder()
                .id(id)
                .username(username)
                .fullname(fullname)
                .password(hashedPassword)
                .role(role)
                .build();
        final User user2 = User.builder()
                .id(id2)
                .username(username2)
                .fullname(fullname)
                .password(hashedPassword)
                .role(role)
                .build();

        final List<User> userList = List.of(user, user2);
        given(userRepository.findAll()).willReturn(userList);

        //when
        List<UserDto> userDtoList = userService.findAllEntity();

        //then
        assertEquals(2, userDtoList.size());
        assertThat(userDtoList)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        new UserDto(id, username, fullname, role),
                        new UserDto(id2, username2, fullname, role)
                );
    }

    @Test
    @DisplayName("should return an empty list")
    void shouldReturnAnEmptyList() {
        //given
        final List<User> userList = List.of();
        given(userRepository.findAll()).willReturn(userList);

        //when
        List<UserDto> userDtoList = userService.findAllEntity();

        //then
        assertEquals(0, userDtoList.size());
    }

    @Test
    @DisplayName("should return UserUpdateDto with empty password")
    void shouldReturnUserUpdateDto() {
        //given
        final int id = 1;
        final String role = "USER";
        final String fullname = "fullname";
        final String username = "username";
        final String hashedPassword = "hashedPassword";
        final User existingUser = User.builder()
                .id(id)
                .username(username)
                .fullname(fullname)
                .password(hashedPassword)
                .role(role)
                .build();

        given(userRepository.findById(id)).willReturn(Optional.of(existingUser));

        //when
        final UserUpdateDto userUpdateDto = userService.getEntityUpdateDto(id);

        //then
        assertEquals(fullname, userUpdateDto.getFullname());
        assertEquals("", userUpdateDto.getPassword());
        assertEquals(role, userUpdateDto.getRole());
        assertEquals(id, userUpdateDto.getId());
        assertEquals(username, userUpdateDto.getUsername());
    }

    @Test
    @DisplayName("should throw IllegalArgumentException if id is invalid")
    void shouldThrowIllegalArgumentExceptionIfIdIsInvalid() {
        //given
        final int id = 0;
        given(userRepository.findById(id)).willReturn(Optional.empty());

        //when & then
        assertThrows(IllegalArgumentException.class, () -> userService.getEntityUpdateDto(id));
    }

    @Test
    @DisplayName("should update existing user with new password")
    void shouldUpdateExistingUserWithNewPassword() {
        //given
        final int id = 1;
        final String role = "USER";
        final String fullname = "fullname";
        final String username = "username";
        final String hashedPassword = "hashedPassword";
        final String updatedFullname = "updatedFullname";
        final String updatedRole = "ADMIN";
        final String updatedUsername = "username";
        final String updatedPassword = "updatedPassword";
        final String updatedHashedPassword = "updatedHashedPassword";
        final User existingUser = User.builder()
                .id(id)
                .username(username)
                .fullname(fullname)
                .password(hashedPassword)
                .role(role)
                .build();
        final UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .id(id)
                .username(updatedUsername)
                .fullname(updatedFullname)
                .password(updatedPassword)
                .role(updatedRole)
                .build();

        given(userRepository.findByUsername(updatedUsername)).willReturn(Optional.of(existingUser));
        given(userRepository.findById(userUpdateDto.getId())).willReturn(Optional.of(existingUser));
        given(passwordEncoder.encode(updatedPassword)).willReturn(updatedHashedPassword);

        //when
        userService.handleEntityUpdate(userUpdateDto);

        //then
        verify(userRepository).save(userCaptor.capture());
        final User updatedUser = userCaptor.getValue();
        assertEquals(updatedFullname, updatedUser.getFullname());
        assertEquals(updatedHashedPassword, updatedUser.getPassword());
        assertEquals(updatedRole, updatedUser.getRole());
        assertEquals(updatedUsername, updatedUser.getUsername());
    }

    @Test
    @DisplayName("should update existing user except password")
    void shouldUpdateExistingUserExceptPassword() {
        //given
        final int id = 1;
        final String role = "USER";
        final String updatedRole = "ADMIN";
        final String fullname = "fullname";
        final String username = "username";
        final String hashedPassword = "hashedPassword";
        final String updatedFullname = "updatedFullname";
        final String updatedUsername = "updatedUsername";
        final User existingUser = User.builder()
                .id(id)
                .username(username)
                .fullname(fullname)
                .password(hashedPassword)
                .role(role)
                .build();
        final UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .id(id)
                .username(updatedUsername)
                .fullname(updatedFullname)
                .password("")
                .role(updatedRole)
                .build();

        given(userRepository.findByUsername(updatedUsername)).willReturn(Optional.empty());
        given(userRepository.findById(userUpdateDto.getId())).willReturn(Optional.of(existingUser));

        //when
        userService.handleEntityUpdate(userUpdateDto);

        //then
        verify(userRepository).save(userCaptor.capture());
        final User updatedUser = userCaptor.getValue();
        assertEquals(updatedFullname, updatedUser.getFullname());
        assertEquals(hashedPassword, updatedUser.getPassword());
        assertEquals(updatedRole, updatedUser.getRole());
        assertEquals(updatedUsername, updatedUser.getUsername());
    }

    @Test
    @DisplayName("should throw EntityExistsException if username already taken")
    void shouldThrowEntityExistsExceptionIfUsernameAlreadyTaken() {
        //given
        final int id = 1;
        final int id2 = 2;
        final String role = "USER";
        final String updatedRole = "ADMIN";
        final String fullname = "fullname";
        final String username = "username";
        final String hashedPassword = "hashedPassword";
        final String updatedFullname = "updatedFullname";
        final String updatedUsername = "updatedUsername";
        final User existingUser = User.builder()
                .id(id2)
                .username(username)
                .fullname(fullname)
                .password(hashedPassword)
                .role(role)
                .build();
        final UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .id(id)
                .username(updatedUsername)
                .fullname(updatedFullname)
                .password("")
                .role(updatedRole)
                .build();
        given(userRepository.findByUsername(updatedUsername)).willReturn(Optional.of(existingUser));

        //when
        assertThrows(EntityExistsException.class, ()-> userService.handleEntityUpdate(userUpdateDto));
        verify(userRepository, times(0)).save(any(User.class));
    }

}