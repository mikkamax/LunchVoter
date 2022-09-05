package com.mike.lunchvoter.api;

import com.mike.lunchvoter.dto.UserDto;
import com.mike.lunchvoter.validation.ValidateOnCreate;
import com.mike.lunchvoter.validation.ValidateOnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequestMapping(value = ProfileApi.PROFILE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profile API", description = "User profile management")
@Validated
public interface ProfileApi {

    String PROFILE_URL = "/api/v1/profile";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(ValidateOnCreate.class)
    @Operation(
            summary = "New user registration",
            description = """
                    Returns created user. No permission required.
                                        
                    Excluded (may provide null value as well) properties:
                    - id
                    - role
                    - enabled
                    """
    )
    UserDto register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "name": "John",
                                                "email": "john@mail.com",
                                                "password": "string"
                                            }"""
                            )
                    )
            )
            @Valid @RequestBody UserDto userDto);

    @GetMapping
    @PreAuthorize("hasAuthority('profile:read')")
    @SecurityRequirement(name = "user")
    @Operation(
            summary = "Get authenticated user profile",
            description = "Returns user. Requires *profile:read* permission."
    )
    UserDto getProfile(@NotNull Authentication authentication);

    @PutMapping
    @Validated(ValidateOnUpdate.class)
    @PreAuthorize("hasAuthority('profile:update')")
    @SecurityRequirement(name = "user")
    @Operation(
            summary = "Update authenticated user profile",
            description = """
                    Returns updated user. Requires *profile:update* permission.
                                
                    Excluded (may provide null value as well) properties:
                    - role
                    - enabled

                    Optional properties:
                    - password
                    """
    )
    UserDto update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": 2,
                                                "name": "Peter the profile",
                                                "email": "peter@mail.com",
                                                "password": "newpass"
                                            }"""
                            )
                    )
            )
            @Valid @RequestBody UserDto userDto,
            @NotNull Authentication authentication);

    @DeleteMapping
    @PreAuthorize("hasAuthority('profile:delete')")
    @SecurityRequirement(name = "user")
    @Operation(
            summary = "Delete authenticated user",
            description = "Returns nothing if user successfully deleted. Requires *profile:delete* permission."
    )
    void delete(@NotNull Authentication authentication);

}
