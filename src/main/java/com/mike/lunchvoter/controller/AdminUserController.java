package com.mike.lunchvoter.controller;

import com.mike.lunchvoter.dto.UserDto;
import com.mike.lunchvoter.exception.IllegalRequestDataException;
import com.mike.lunchvoter.security.Role;
import com.mike.lunchvoter.service.UserService;
import com.mike.lunchvoter.validation.ValidateOnAdminCreate;
import com.mike.lunchvoter.validation.ValidateOnAdminUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = AdminUserController.ADMIN_URL)
@Validated
public class AdminUserController {

    static final String ADMIN_URL = "/api/v1/admin/users";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(ValidateOnAdminCreate.class)
    @PreAuthorize("hasAuthority('user:create')")
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        userDto.setRegistrationDate(LocalDate.now());
        userDto.setPassword(
                passwordEncoder.encode(userDto.getPassword())
        );
        return userService.create(userDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public UserDto get(@NotNull @PathVariable("id") Integer userId) {
        return userService.get(userId);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public List<UserDto> getAllByParams(@Nullable @RequestParam(required = false) String name,
                                        @Nullable @RequestParam(required = false) String email,
                                        @Nullable @RequestParam(required = false) Boolean enabled,
                                        @Nullable @RequestParam(required = false) Role role) {
        if (name == null && email == null && enabled == null && role == null) {
            return userService.getAll();
        }

        return userService.getByParams(name, email, enabled, role);
    }

    @PutMapping("/{id}")
    @Validated(ValidateOnAdminUpdate.class)
    @PreAuthorize("hasAuthority('user:update')")
    public UserDto update(@NotNull @PathVariable("id") Integer userId,
                          @Valid @RequestBody UserDto userDto) {
        if (!Objects.equals(userId, userDto.getId())) {
            throw new IllegalRequestDataException(userDto + " id doesn't match path id = " + userId);
        }

        if (userDto.getPassword() != null) {
            userDto.setPassword(
                    passwordEncoder.encode(userDto.getPassword())
            );
        }

        return userService.update(userId, userDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public void delete(@NotNull @PathVariable("id") Integer userId) {
        userService.delete(userId);
    }

}
