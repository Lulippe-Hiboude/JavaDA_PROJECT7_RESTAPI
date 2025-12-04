package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserCreateDto;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.dto.UserUpdateDto;
import com.nnk.springboot.mapper.UserMapper;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void handleUserCreation(final UserCreateDto userCreatedto) {
        log.info("Handle user creation");
        userRepository.findByUsername(userCreatedto.getUsername())
                .ifPresentOrElse(existingUser -> {
                            log.error("User already exists");
                            throw new EntityExistsException("the username " + userCreatedto.getUsername() + " is already taken");
                        },
                        () -> createUser(userCreatedto));
    }

    public void handleUserDeletion(final Integer id) {
        userRepository.findById(id)
                .ifPresentOrElse(
                        existingUser -> userRepository.deleteById(id),
                        () -> {
                            throw new EntityNotFoundException("the user with id " + id + " was not found");
                        }
                );
    }

    public List<UserDto> findAll() {
        return UserMapper.INSTANCE.toUserDtoList(userRepository.findAll());
    }

    public UserUpdateDto getUserUpdateDto(final Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        return UserMapper.INSTANCE.toUserUpdateDto(user);

    }

    public void handleUserUpdate(final UserUpdateDto userUpdateDto) {
        Optional<User> existingUser = userRepository.findByUsername(userUpdateDto.getUsername());
        if (isUsernameInvalid(userUpdateDto, existingUser)) {
            throw new EntityExistsException("the username " + userUpdateDto.getUsername() + " is already taken");
        }

        BiFunction<UserUpdateDto, User, User> mapper = getMapper(userUpdateDto);

        userRepository.findById(userUpdateDto.getId())
                .ifPresent(userToUpdate -> userRepository.save(
                                mapper.apply(userUpdateDto, userToUpdate))
                );
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

    private void createUser(final UserCreateDto userCreateDto) {
        final String hashedPassword = passwordEncoder.encode(userCreateDto.getPassword());
        userRepository.save(UserMapper.INSTANCE.toUser(userCreateDto, hashedPassword));
        log.debug("user created");
    }

    private static boolean isUsernameInvalid(final UserUpdateDto userUpdateDto, final Optional<User> existingUser) {
        return existingUser.isPresent() &&
                !Objects.equals(existingUser.get().getId(), userUpdateDto.getId());
    }
}
