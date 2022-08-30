package com.mike.lunchvoter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mike.lunchvoter.security.Role;
import com.mike.lunchvoter.validation.ValidateOnAdminCreate;
import com.mike.lunchvoter.validation.ValidateOnAdminUpdate;
import com.mike.lunchvoter.validation.ValidateOnCreate;
import com.mike.lunchvoter.validation.ValidateOnUpdate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    /**
     * User's id
     */
    @Null(groups = {ValidateOnCreate.class, ValidateOnAdminCreate.class})
    @NotNull(groups = {ValidateOnUpdate.class, ValidateOnAdminUpdate.class})
    private Integer id;

    /**
     * User's name
     */
    @NotBlank
    private String name;

    /**
     * User's email (used as login)
     */
    @NotBlank
    @Email
    private String email;

    /**
     * User's password
     */
    @NotBlank(groups = {ValidateOnCreate.class, ValidateOnAdminCreate.class})
    @Size(min = 5)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * User's role
     */
    @Null(groups = {ValidateOnCreate.class, ValidateOnUpdate.class})
    @NotNull(groups = {ValidateOnAdminCreate.class, ValidateOnAdminUpdate.class})
    private Role role;

    /**
     * User's registration date
     */
    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate registrationDate;

    /**
     * Is user active
     */
    @Null(groups = {ValidateOnCreate.class, ValidateOnUpdate.class})
    @NotNull(groups = {ValidateOnAdminCreate.class, ValidateOnAdminUpdate.class})
    private Boolean enabled;

}
