package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.user.UserCreateDto;
import com.nnk.springboot.dto.user.UserDto;
import com.nnk.springboot.dto.user.UserUpdateDto;
import com.nnk.springboot.service.impl.UserServiceImpl;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    void displayAddUserForm_shouldReturnAddViewWithEmptyModel() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to user list when valid userCreateDto")
    void redirectToUserListWhenUserCreateDtoValid() throws Exception {
        //given
        final String username = "username";
        final String fullname = "fullname";
        final String user = "USER";
        final String password = "Password@1";

        // when & then
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", username)
                        .param("fullname", fullname)
                        .param("role", user)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userServiceImpl).handleEntityCreation(any(UserCreateDto.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to user add form if parameters are invalid")
    void redirectToUserAddFormIfParametersIsInvalid() throws Exception {
        //given
        final String invalidUsername = "";
        final String invalidFullname = null;
        final String invalidRole = "INVALID";
        final String invalidPassword = "invalidPassword";

        // when & then
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", invalidUsername)
                        .param("fullname", invalidFullname)
                        .param("role", invalidRole)
                        .param("password", invalidPassword))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeHasFieldErrors("user", "username", "fullname", "role", "password"));

        verify(userServiceImpl, times(0)).handleEntityCreation(any(UserCreateDto.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to user add form if username already exists")
    void redirectToUserAddFormIfUsernameAlreadyExists() throws Exception {
        //given
        final String username = "username";
        final String fullname = "fullname";
        final String user = "USER";
        final String password = "Password@1";
        doThrow(new EntityExistsException("username already taken")).when(userServiceImpl).handleEntityCreation(any(UserCreateDto.class));

        //when & then
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", username)
                        .param("fullname", fullname)
                        .param("role", user)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeHasFieldErrors("user", "username"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to user list after deletion by id")
    void redirectToUserListAfterDeletionById() throws Exception {
        //given
        final int userId = 1;

        //when & then
        mockMvc.perform(get("/user/delete/" + userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(flash().attribute("successMessage", "user deleted successfully"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to user list after throw EntityNotFoundException")
    void redirectToUserListAfterThrowEntityNotFoundException() throws Exception {
        //given
        final int userId = 1;
        doThrow(new EntityNotFoundException("user not found")).when(userServiceImpl).handleEntityDeletion(userId);

        //when & then
        mockMvc.perform(get("/user/delete/" + userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(flash().attribute("errorMessage", "user not found"));

    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to user List after retrieving all users")
    void redirectToUserListAfterRetrievingAllUsers() throws Exception {
        //given
        final int id = 1;
        final int id2 = 2;
        final String role = "USER";
        final String fullname = "fullname";
        final String username = "username";
        final String username2 = "username2";
        final UserDto userDto1 = UserDto.builder()
                .id(id)
                .username(username)
                .fullname(fullname)
                .role(role)
                .build();

        final UserDto userDto2 = UserDto.builder()
                .id(id2)
                .username(username2)
                .fullname(fullname)
                .role(role)
                .build();

        final List<UserDto> userDtoList = List.of(userDto1, userDto2);
        given(userServiceImpl.findAllEntity()).willReturn(userDtoList);

        //when & then
        mockMvc.perform(get("/user/list")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attribute("users", userDtoList));

    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to user List even if users is empty")
    void redirectToUserListEmpty() throws Exception {
        //given
        List<UserDto> emptyList = List.of();
        given(userServiceImpl.findAllEntity()).willReturn(emptyList);

        //when & then
        mockMvc.perform(get("/user/list")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attribute("users", emptyList));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should return user/update form")
    void redirectToUserUpdateForm() throws Exception {
        //given
        final int id = 1;
        final String role = "USER";
        final String fullname = "fullname";
        final String username = "username";
        final UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .id(id)
                .username(username)
                .fullname(fullname)
                .password("")
                .role(role)
                .build();

        given(userServiceImpl.getEntityUpdateDto(id)).willReturn(userUpdateDto);

        //when
        mockMvc.perform(get("/user/update/"+ id)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attribute("user", userUpdateDto));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to userList after successfully update")
    void redirectToUserListAfterSuccessfulUpdate() throws Exception {
        //given
        final int id = 1;
        final String username = "username";
        final String fullname = "fullname";
        final String role = "USER";
        final String updatedPassword = "Password@1";
        final UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .id(id)
                .username(username)
                .fullname(fullname)
                .password(updatedPassword)
                .role(role)
                .build();

        // when & then
        mockMvc.perform(post("/user/update/{id}", id)
                        .with(csrf())
                        .param("username", username)
                        .param("fullname", fullname)
                        .param("role", role)
                        .param("password", updatedPassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userServiceImpl).handleEntityUpdate(userUpdateDto);
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to user update form if username already taken")
    void redirectToUserUpdateFormIfUsernameAlreadyTaken() throws Exception {
        //given
        final int id = 1;
        final String username = "username";
        final String fullname = "fullname";
        final String role = "USER";
        final String updatedPassword = "Password@1";
        final UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .id(id)
                .username(username)
                .fullname(fullname)
                .password(updatedPassword)
                .role(role)
                .build();
        doThrow(new EntityExistsException("username already taken")).when(userServiceImpl).handleEntityUpdate(userUpdateDto);

        //when & then
        mockMvc.perform(post("/user/update/{id}", id)
                .with(csrf())
                .param("username", username)
                .param("fullname", fullname)
                .param("role", role)
                .param("password", updatedPassword))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeHasFieldErrors("user", "username"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to user update form if parameters are invalids")
    void redirectToUserUpdateFormInvalidParameters() throws Exception {
        //given
        final int id = 1;
        final String username = "";
        final String fullname = null;
        final String role = "INVALID";
        final String updatedPassword = "invalidPassword";
        final UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .id(id)
                .username(username)
                .fullname(fullname)
                .password(updatedPassword)
                .role(role)
                .build();
        //when & then
        mockMvc.perform(post("/user/update/{id}", id)
                        .with(csrf())
                        .param("username", username)
                        .param("fullname", fullname)
                        .param("role", role)
                        .param("password", updatedPassword))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeHasFieldErrors("user", "username", "fullname", "role", "password"));

        verify(userServiceImpl,times(0)).handleEntityUpdate(userUpdateDto);
    }
}