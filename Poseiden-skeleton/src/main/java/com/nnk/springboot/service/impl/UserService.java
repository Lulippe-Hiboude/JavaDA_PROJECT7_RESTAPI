package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.user.UserCreateDto;
import com.nnk.springboot.dto.user.UserDto;
import com.nnk.springboot.dto.user.UserUpdateDto;
import com.nnk.springboot.mapper.UserMapper;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.AbstractEntityService;
import com.nnk.springboot.service.EntityService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
@Transactional
@Slf4j
public class UserService extends AbstractEntityService<User, UserCreateDto, UserDto, UserUpdateDto>
        implements EntityService<UserCreateDto, UserDto, UserUpdateDto> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    protected void processEntityCreation(final UserCreateDto userCreatedto) {
        userRepository.findByUsername(userCreatedto.getUsername())
                .ifPresentOrElse(existingUser -> {
                            log.error("User already exists");
                            throw new EntityExistsException("the username " + userCreatedto.getUsername() + " is already taken");
                        },
                        () -> super.createEntity(userCreatedto));
    }

    protected void handleError(final Integer id) {
        throw new EntityNotFoundException("the user with id " + id + " was not found");
    }

    protected List<UserDto> toDtoList(final List<User> users) {

        return UserMapper.INSTANCE.toUserDtoList(users);
    }

    protected UserUpdateDto getEntityUpdateDto(final User user) {
        return UserMapper.INSTANCE.toUserUpdateDto(user);

    }

    protected void checkEntityValidity(final UserUpdateDto userUpdateDto) {
        Optional<User> existingUser = userRepository.findByUsername(userUpdateDto.getUsername());
        if (isUsernameInvalid(userUpdateDto, existingUser)) {
            throw new EntityExistsException("the username " + userUpdateDto.getUsername() + " is already taken");
        }
    }

    protected Integer getEntityId(final UserUpdateDto userUpdateDto) {
        return userUpdateDto.getId();
    }

    protected User getUpdatedEntity(final UserUpdateDto userUpdateDto, final User userToUpdate) {
        BiFunction<UserUpdateDto, User, User> mapper = getMapper(userUpdateDto);
        return mapper.apply(userUpdateDto, userToUpdate);
    }

    private BiFunction<UserUpdateDto, User, User> getMapper(final UserUpdateDto userUpdateDto) {
        if (Strings.isBlank(userUpdateDto.getPassword())) {
            return UserMapper.INSTANCE::toUser;
        }

        return (dto, user) -> {
            final String hashedPassword = passwordEncoder.encode(dto.getPassword());
            return UserMapper.INSTANCE.toUser(dto, hashedPassword, user);
        };
    }

    protected User getEntity(final UserCreateDto userCreateDto) {
        final String hashedPassword = passwordEncoder.encode(userCreateDto.getPassword());
        return UserMapper.INSTANCE.toUser(userCreateDto, hashedPassword);
    }

    private static boolean isUsernameInvalid(final UserUpdateDto userUpdateDto, final Optional<User> existingUser) {
        return existingUser.isPresent() &&
                !Objects.equals(existingUser.get().getId(), userUpdateDto.getId());
    }
}
