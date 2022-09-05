package com.mike.lunchvoter.controller;

import com.mike.lunchvoter.api.ProfileApi;
import com.mike.lunchvoter.dto.UserDto;
import com.mike.lunchvoter.security.Role;
import com.mike.lunchvoter.service.UserService;
import com.mike.lunchvoter.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@RestController
public class ProfileController implements ProfileApi {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        userDto.setRegistrationDate(LocalDate.now());
        userDto.setRole(Role.USER);
        userDto.setPassword(
                passwordEncoder.encode(userDto.getPassword())
        );
        userDto.setEnabled(true);
        return userService.create(userDto);
    }

    @Override
    public UserDto getProfile(@NotNull Authentication authentication) {
        return userService.get(
                SecurityUtil.getAuthenticatedUserIdOrElseThrow(authentication)
        );
    }

    @Override
    public UserDto update(@Valid @RequestBody UserDto userDto,
                          @NotNull Authentication authentication) {

        Long userId = SecurityUtil.getAuthenticatedUserIdOrElseThrow(authentication);

        checkIfAuthorizedUserIdMatchesUserDtoIdOrElseThrow(userId, userDto.getId());

        if (Objects.nonNull(userDto.getPassword())) {
            userDto.setPassword(
                    passwordEncoder.encode(userDto.getPassword())
            );
        }

        return userService.update(userId, userDto);
    }

    @Override
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
