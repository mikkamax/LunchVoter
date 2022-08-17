package com.mike.lunchvoter.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "menu")
public class Menu {

    @Id
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @NotNull
    @Column(name = "date")
    private LocalDate date;

}
