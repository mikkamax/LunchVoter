package com.mike.lunchvoter.entity;

import com.mike.lunchvoter.security.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "user_password")
    private String password;

    @NotNull
    @Column(name = "registration_date", updatable = false)
    private LocalDate registrationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Role role;

    @NotNull
    @Column(name = "enabled")
    private Boolean enabled;

}
