package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.user.UserCreateDto;
import com.nnk.springboot.dto.user.UserDto;
import com.nnk.springboot.dto.user.UserUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "username", source = "userCreateDto.username")
    @Mapping(target = "password", source = "hashedPassword")
    @Mapping(target = "fullname", source = "userCreateDto.fullname")
    @Mapping(target = "role", source = "userCreateDto.role")
    @Mapping(target = "id", ignore = true)
    User toUser(final UserCreateDto userCreateDto,final String hashedPassword);

    @Mapping(target = "username", source = "userUpdateDto.username")
    @Mapping(target = "password", source = "hashedPassword")
    @Mapping(target = "fullname", source = "userUpdateDto.fullname")
    @Mapping(target = "role", source = "userUpdateDto.role")
    @Mapping(target = "id", ignore = true)
    User toUser(final UserUpdateDto userUpdateDto,final String hashedPassword,@MappingTarget final User user);

    @Mapping(target = "username", source = "userUpdateDto.username")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "fullname", source = "userUpdateDto.fullname")
    @Mapping(target = "role", source = "userUpdateDto.role")
    @Mapping(target = "id", ignore = true)
    User toUser(final UserUpdateDto userUpdateDto,@MappingTarget final User user);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "fullname", source = "fullname")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "id", source = "id")
    UserDto toUserDto(final User user);

    default List<UserDto> toUserDtoList(final List<User> users){
        return users.stream()
                .map(this::toUserDto)
                .toList();
    }
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", constant = "")
    @Mapping(target = "fullname", source = "fullname")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "id", source = "id")
    UserUpdateDto toUserUpdateDto(final User user);
}
