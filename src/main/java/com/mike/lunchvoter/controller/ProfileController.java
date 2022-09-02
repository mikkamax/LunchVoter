package com.mike.lunchvoter.controller;

import com.mike.lunchvoter.dto.UserDto;
import com.mike.lunchvoter.security.Role;
import com.mike.lunchvoter.service.UserService;
import com.mike.lunchvoter.util.SecurityUtil;
import com.mike.lunchvoter.validation.ValidateOnCreate;
import com.mike.lunchvoter.validation.ValidateOnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping(value = ProfileController.USER_URL)
@Validated
public class ProfileController {

    static final String USER_URL = "/api/v1/profile";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(ValidateOnCreate.class)
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        userDto.setRegistrationDate(LocalDate.now());
        userDto.setRole(Role.USER);
        userDto.setPassword(
                passwordEncoder.encode(userDto.getPassword())
        );
        userDto.setEnabled(true);
        return userService.create(userDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('profile:read')")
    public UserDto getProfile(@NotNull Authentication authentication) {
        return userService.get(
                SecurityUtil.getAuthenticatedUserIdOrElseThrow(authentication)
        );
    }

    @PutMapping
    @Validated(ValidateOnUpdate.class)
    @PreAuthorize("hasAuthority('profile:update')")
    public UserDto update(@Valid @RequestBody UserDto userDto,
                          @NotNull Authentication authentication) {

        Long userId = SecurityUtil.getAuthenticatedUserIdOrElseThrow(authentication);

        checkIfAuthorizedUserIdMatchesUserDtoIdOrElseThrow(userId, userDto.getId());

        if (userDto.getPassword() != null) {
            userDto.setPassword(
                    passwordEncoder.encode(userDto.getPassword())
            );
        }

        return userService.update(userId, userDto);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('profile:delete')")
    public void delete(@NotNull Authentication authentication) {
        userService.delete(
                SecurityUtil.getAuthenticatedUserIdOrElseThrow(authentication)
        );
    }

    private void checkIfAuthorizedUserIdMatchesUserDtoIdOrElseThrow(Long authorizedUserId, Long userDtoId) {
        if (!Objects.equals(authorizedUserId, userDtoId)) {
            throw new IllegalCallerException("Illegal attempt to update user profile with id = " + userDtoId
                    + " by user with id = " + authorizedUserId);
        }
    }

}
