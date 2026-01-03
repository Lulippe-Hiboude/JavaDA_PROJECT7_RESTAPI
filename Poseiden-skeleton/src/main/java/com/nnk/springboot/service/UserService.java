package com.nnk.springboot.service;

import com.nnk.springboot.dto.user.UserCreateDto;
import com.nnk.springboot.dto.user.UserDto;
import com.nnk.springboot.dto.user.UserUpdateDto;

public interface UserService extends EntityService<UserCreateDto, UserDto, UserUpdateDto> {
}
