package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.user.UserCreateDto;
import com.nnk.springboot.dto.user.UserDto;
import com.nnk.springboot.dto.user.UserUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper extends BaseMapper<User, UserCreateDto, UserDto, UserUpdateDto> {

    String EMPTY_STRING = "";

    @Mapping(target = "username", source = "userCreateDto.username")
    @Mapping(target = "password", source = "hashedPassword")
    @Mapping(target = "fullname", source = "userCreateDto.fullname")
    @Mapping(target = "role", source = "userCreateDto.role")
    @Mapping(target = "id", ignore = true)
    User toUser(final UserCreateDto userCreateDto, final String hashedPassword);

    @Mapping(target = "username", source = "userUpdateDto.username")
    @Mapping(target = "password", source = "hashedPassword")
    @Mapping(target = "fullname", source = "userUpdateDto.fullname")
    @Mapping(target = "role", source = "userUpdateDto.role")
    @Mapping(target = "id", ignore = true)
    User toUser(final UserUpdateDto userUpdateDto, final String hashedPassword, @MappingTarget final User user);

    @Mapping(target = "username", source = "userUpdateDto.username")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "fullname", source = "userUpdateDto.fullname")
    @Mapping(target = "role", source = "userUpdateDto.role")
    @Mapping(target = "id", ignore = true)
    User toEntity(final UserUpdateDto userUpdateDto, @MappingTarget final User user);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", constant = EMPTY_STRING)
    @Mapping(target = "fullname", source = "fullname")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "id", source = "id")
    UserUpdateDto toUpdateDto(final User user);
}
