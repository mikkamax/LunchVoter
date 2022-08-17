package com.mike.lunchvoter.dto;

import java.time.LocalDate;
import java.util.List;

public class MenuDto {
    private Integer id;
    private Integer restaurantId;
    private LocalDate date;
    private List<DishDto> dishes;
    private Integer votesCount;
}
