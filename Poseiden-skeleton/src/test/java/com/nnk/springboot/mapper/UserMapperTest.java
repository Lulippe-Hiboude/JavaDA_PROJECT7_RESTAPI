package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserCreateDto;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.dto.UserUpdateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Test
    @DisplayName("should map a userCreateDto to User")
    void shouldMapUserCreateDtoToUser() {
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

        //when
        final User expectedUser = UserMapper.INSTANCE.toUser(userCreateDto,hashedPassword);

        //then
        assertEquals(username,expectedUser.getUsername());
        assertEquals(fullname,expectedUser.getFullname());
        assertEquals(hashedPassword,expectedUser.getPassword());
        assertEquals(role,expectedUser.getRole());
    }

    @Test
    @DisplayName("should return null if userCreateDto is null")
    void shouldReturnNullIfUserCreateDtoIsNull() {
        //given
        final UserCreateDto userCreateDto = null;
        //when
        final User expectedUser = UserMapper.INSTANCE.toUser(userCreateDto,null);

        //then
        assertNull(expectedUser);
    }

    @Test
    @DisplayName("should return a userDto")
    void shouldMapUserDtoToUser() {
        //given
        final int id = 1;
        final String role = "USER";
        final String fullname = "fullname";
        final String username = "username";
        final String hashedPassword = "hashedPassword";
        final User user = User.builder()
                .id(id)
                .username(username)
                .fullname(fullname)
                .password(hashedPassword)
                .role(role)
                .build();

        //when
        final UserDto userDto = UserMapper.INSTANCE.toUserDto(user);

        //then
        assertEquals(id,userDto.getId());
        assertEquals(username,userDto.getUsername());
        assertEquals(fullname,userDto.getFullname());
        assertEquals(role,userDto.getRole());
    }
    @Test
    @DisplayName("should return a list of UserDto")
    void shouldMapListOfUserDtoToUser() {
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

        final List<User> userList = List.of(user, user2);

        //when
        final List<UserDto> userDtoList = UserMapper.INSTANCE.toUserDtoList(userList);

        //then
        assertEquals(2, userDtoList.size());
        assertThat(userDtoList)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(userDto1,userDto2);

    }

    @Test
    @DisplayName("should return a empty list of UserDto")
    void shouldReturnEmptyListOfUserDtoToUser() {
        //given
        final List<User> userList = List.of();

        //when
        final List<UserDto> userDtoList = UserMapper.INSTANCE.toUserDtoList(userList);

        //then
        assertEquals(0, userDtoList.size());
    }

    @Test
    @DisplayName("should return null if user is null")
    void shouldReturnNullIfUserIsNull() {
        final UserDto userDto = UserMapper.INSTANCE.toUserDto(null);
        assertNull(userDto);
    }

    @Test
    @DisplayName("should update all information of an existing user")
    void shouldUpdateExistingUser() {
        //given
        final int id = 1;
        final String role = "USER";
        final String updatedRole = "ADMIN";
        final String fullname = "fullname";
        final String username = "username";
        final String hashedPassword = "hashedPassword";
        final String updatedFullname = "updatedFullname";
        final String updatedUsername = "updatedUsername";
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
        //when
        final User updatedUser = UserMapper.INSTANCE.toUser(userUpdateDto,updatedHashedPassword,existingUser);

        //then
        assertEquals(id,updatedUser.getId());
        assertEquals(updatedUsername,updatedUser.getUsername());
        assertEquals(updatedFullname,updatedUser.getFullname());
        assertEquals(updatedHashedPassword,updatedUser.getPassword());
        assertEquals(updatedRole,updatedUser.getRole());
    }

    @Test
    @DisplayName("Should update an existing user except password")
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
        final String updatedPassword = "updatedPassword";
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
        //when
        final User updatedUser = UserMapper.INSTANCE.toUser(userUpdateDto,existingUser);

        //then
        assertEquals(id,updatedUser.getId());
        assertEquals(updatedUsername,updatedUser.getUsername());
        assertEquals(updatedFullname,updatedUser.getFullname());
        assertEquals(hashedPassword,updatedUser.getPassword());
        assertEquals(updatedRole,updatedUser.getRole());
    }

    @Test
    @DisplayName("should map a user to userUpdateDto with empty password")
    void shouldMapUserToUserUpdateDtoWithEmptyPassword() {
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

        //when
        final UserUpdateDto userUpdateDto = UserMapper.INSTANCE.toUserUpdateDto(existingUser);

        //then
        assertEquals(id,userUpdateDto.getId());
        assertEquals(username,userUpdateDto.getUsername());
        assertEquals(fullname,userUpdateDto.getFullname());
        assertEquals("",userUpdateDto.getPassword());
        assertEquals(role,userUpdateDto.getRole());
    }

}