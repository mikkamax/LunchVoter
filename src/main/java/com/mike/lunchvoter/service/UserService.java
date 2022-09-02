package com.mike.lunchvoter.service;

import com.mike.lunchvoter.dto.UserDto;
import com.mike.lunchvoter.entity.User;
import com.mike.lunchvoter.exception.CustomConstraintViolationException;
import com.mike.lunchvoter.exception.ObjectNotFoundException;
import com.mike.lunchvoter.mapping.UserMapper;
import com.mike.lunchvoter.repository.UserRepository;
import com.mike.lunchvoter.security.Role;
import com.mike.lunchvoter.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private static final ExampleMatcher EXAMPLE_MATCHER_FOR_GET_BY_PARAMS = ExampleMatcher.matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase()
            .withIgnoreNullValues();

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDto create(UserDto userDto) {
        checkForNoConstraintViolationsOrElseThrow(userDto);

        User user = userMapper.mapToEntity(userDto);

        return userMapper.mapToDto(
                userRepository.save(user)
        );
    }

    public UserDto get(Long userId) {
        return userMapper.mapToDto(
                getUserByIdOrElseThrow(userId)
        );
    }

    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToDto)
                .toList();
    }

    public List<UserDto> getByParams(String name, String email, Boolean enabled, Role role) {
        Example<User> example = Example.of(
                new User()
                        .setName(name)
                        .setEmail(email)
                        .setEnabled(enabled)
                        .setRole(role),
                EXAMPLE_MATCHER_FOR_GET_BY_PARAMS
        );

        return userRepository.findAll(example).stream()
                .map(userMapper::mapToDto)
                .toList();
    }

    @Transactional
    public UserDto update(Long userId, UserDto userDto) {
        User userToUpdate = getUserByIdOrElseThrow(userId);

        checkForNoConstraintViolationsOrElseThrow(userDto);
        User updatedUser = userMapper.mapToEntityOnUpdate(userDto, userToUpdate);

        return userMapper.mapToDto(
                userRepository.save(updatedUser)
        );
    }

    @Transactional
    public void delete(Long userId) {
        checkIfUSerWithThisIdExistsOrElseThrow(userId);
        userRepository.deleteById(userId);
    }

    private User getUserByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> objectNotFoundException(userId));
    }

    private void checkIfUSerWithThisIdExistsOrElseThrow(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw objectNotFoundException(userId);
        }
    }

    private void checkForNoConstraintViolationsOrElseThrow(UserDto userDto) {
        boolean isConstraintViolated = userRepository.existsByIdNotAndEmailEqualsIgnoreCase(
                Optional.ofNullable(userDto.getId()).orElse(Constants.NON_EXISTING_ID),
                userDto.getEmail()
        );

        if (isConstraintViolated) {
            throw new CustomConstraintViolationException("Cannot save " + userDto
                    + " because User with same email already exists in the database");
        }
    }

    private static ObjectNotFoundException objectNotFoundException(Long userId) {
        return new ObjectNotFoundException("There is no user with id = " + userId);
    }

}
