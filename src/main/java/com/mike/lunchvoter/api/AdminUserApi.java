package com.mike.lunchvoter.api;

import com.mike.lunchvoter.dto.UserDto;
import com.mike.lunchvoter.security.Role;
import com.mike.lunchvoter.validation.ValidateOnAdminCreate;
import com.mike.lunchvoter.validation.ValidateOnAdminUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequestMapping(value = AdminUserApi.ADMIN_USER_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "AdminUser API", description = "Admin user management")
@Validated
@SecurityRequirement(name = "admin")
public interface AdminUserApi {

    String ADMIN_USER_URL = "/api/v1/admin/users";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(ValidateOnAdminCreate.class)
    @PreAuthorize("hasAuthority('user:create')")
    @Operation(
            summary = "Add a new user",
            description = """
                    Returns created user. Requires *user:create* permission.
                                        
                    Excluded (may provide null value as well) properties:
                    - id
                    """
    )
    UserDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "name": "John",
                                                "email": "john@mail.com",
                                                "password": "string",
                                                "role": "USER",
                                                "enabled": true
                                            }"""
                            )
                    )
            )
            @Valid @RequestBody UserDto userDto);

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('user:read')")
    @Operation(
            summary = "Get user by ID",
            description = "Returns single user. Requires *user:read* permission."
    )
    UserDto get(@Parameter(description = "ID of user to return", required = true, example = "1")
                @NotNull @PathVariable Long userId);

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    @Operation(
            summary = "Get users by params",
            description = "Returns list of users containing requested params. " +
                    "If no params provided - returns list of all users. Requires *user:read* permission."
    )
    List<UserDto> getAllByParams(@Nullable @RequestParam(required = false) String name,
                                 @Nullable @RequestParam(required = false) String email,
                                 @Nullable @RequestParam(required = false) Boolean enabled,
                                 @Nullable @RequestParam(required = false) Role role);

    @PutMapping("/{userId}")
    @Validated(ValidateOnAdminUpdate.class)
    @PreAuthorize("hasAuthority('user:update')")
    @Operation(
            summary = "Update an existing user",
            description = """
                    Returns updated user. Requires *user:update* permission.
                                        
                    Optional properties:
                    - password
                                        
                    Constraint: you can't revoke Admin role in case of updating last active Admin in the database.
                    """
    )
    UserDto update(@Parameter(description = "ID of user to update, should match ID in request body", required = true, example = "2")
                   @NotNull @PathVariable Long userId,
                   @io.swagger.v3.oas.annotations.parameters.RequestBody(
                           content = @Content(
                                   examples = @ExampleObject(
                                           value = """
                                                   {
                                                       "id": 2,
                                                       "name": "Peter the man",
                                                       "email": "peter@mail.com",
                                                       "password": "userpass",
                                                       "role": "USER",
                                                       "enabled": true
                                                   }"""
                                   )
                           )
                   )
                   @Valid @RequestBody UserDto userDto);

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('user:delete')")
    @Operation(
            summary = "Delete user by ID",
            description = """
                    Returns nothing if user successfully deleted. Requires *user:delete* permission.
                                        
                    Constraint: you cannot delete last active Admin from the database.
                    """
    )
    void delete(@Parameter(description = "ID of user to delete", required = true, example = "3")
                @NotNull @PathVariable Long userId);

}
