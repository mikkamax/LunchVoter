package com.mike.lunchvoter.dto;

import java.time.LocalDate;

public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private LocalDate registrationDate;
    private Boolean enabled;
}
