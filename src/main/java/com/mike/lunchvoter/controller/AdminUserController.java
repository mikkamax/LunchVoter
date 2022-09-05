package com.mike.lunchvoter.controller;

import com.mike.lunchvoter.api.AdminUserApi;
import com.mike.lunchvoter.dto.UserDto;
import com.mike.lunchvoter.exception.IllegalRequestDataException;
import com.mike.lunchvoter.security.Role;
import com.mike.lunchvoter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
public class AdminUserController implements AdminUserApi {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        userDto.setRegistrationDate(LocalDate.now());
        userDto.setPassword(
                passwordEncoder.encode(userDto.getPassword())
        );
        return userService.create(userDto);
    }

    @Override
    public UserDto get(@NotNull @PathVariable Long userId) {
        return userService.get(userId);
    }

    @Override
    public List<UserDto> getAllByParams(@Nullable @RequestParam(required = false) String name,
                                        @Nullable @RequestParam(required = false) String email,
                                        @Nullable @RequestParam(required = false) Boolean enabled,
                                        @Nullable @RequestParam(required = false) Role role) {
        if (name == null && email == null && enabled == null && role == null) {
            return userService.getAll();
        }

        return userService.getByParams(name, email, enabled, role);
    }

    @Override
    public UserDto update(@NotNull @PathVariable Long userId,
                          @Valid @RequestBody UserDto userDto) {
        if (!Objects.equals(userId, userDto.getId())) {
            throw new IllegalRequestDataException(userDto + " id doesn't match path id = " + userId);
        }

        if (Objects.nonNull(userDto.getPassword())) {
            userDto.setPassword(
                    passwordEncoder.encode(userDto.getPassword())
            );
        }

        return userService.update(userId, userDto);
    }

    @Override
    public void delete(@NotNull @PathVariable Long userId) {
        userService.delete(userId);
    }

}
