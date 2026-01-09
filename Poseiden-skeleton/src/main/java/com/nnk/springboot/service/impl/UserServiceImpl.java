package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.user.UserCreateDto;
import com.nnk.springboot.dto.user.UserDto;
import com.nnk.springboot.dto.user.UserUpdateDto;
import com.nnk.springboot.mapper.UserMapper;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.AbstractEntityService;
import com.nnk.springboot.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
@Transactional
@Slf4j
public class UserServiceImpl extends AbstractEntityService<User, UserCreateDto, UserDto, UserUpdateDto>
        implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(final UserRepository userRepository, final UserMapper userMapper, final PasswordEncoder passwordEncoder) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void processEntityCreation(final UserCreateDto userCreateDto) {
        userRepository.findByUsername(userCreateDto.getUsername())
                .ifPresentOrElse(existingUser -> {
                            log.error("User already exists");
                            throw new EntityExistsException("the username " + userCreateDto.getUsername() + " is already taken");
                        },
                        () -> createEntity(userCreateDto));
    }

    @Override
    protected void handleError(final Integer id) {
        throw new EntityNotFoundException("the user with id " + id + " was not found");
    }

    @Override
    protected void checkEntityValidity(final UserUpdateDto userUpdateDto) {
        Optional<User> existingUser = userRepository.findByUsername(userUpdateDto.getUsername());
        if (isUsernameInvalid(userUpdateDto, existingUser)) {
            throw new EntityExistsException("the username " + userUpdateDto.getUsername() + " is already taken");
        }
    }

    @Override
    protected Integer getEntityId(final UserUpdateDto userUpdateDto) {
        return userUpdateDto.getId();
    }

    @Override
    protected User getUpdatedEntity(final UserUpdateDto userUpdateDto, final User userToUpdate) {
        BiFunction<UserUpdateDto, User, User> mapper = getMapper(userUpdateDto);
        return mapper.apply(userUpdateDto, userToUpdate);
    }

    private BiFunction<UserUpdateDto, User, User> getMapper(final UserUpdateDto userUpdateDto) {
        if (Strings.isBlank(userUpdateDto.getPassword())) {
            return userMapper::toEntity;
        }

        return (dto, user) -> {
            final String hashedPassword = passwordEncoder.encode(dto.getPassword());
            return userMapper.toUser(dto, hashedPassword, user);
        };
    }

    @Override
    protected void createEntity(final UserCreateDto userCreateDto) {
        entityRepository.save(getEntity(userCreateDto));
    }

    private User getEntity(final UserCreateDto userCreateDto) {
        final String hashedPassword = passwordEncoder.encode(userCreateDto.getPassword());
        return userMapper.toUser(userCreateDto, hashedPassword);
    }

    private static boolean isUsernameInvalid(final UserUpdateDto userUpdateDto, final Optional<User> existingUser) {
        return existingUser.isPresent() &&
                !Objects.equals(existingUser.get().getId(), userUpdateDto.getId());
    }
}
