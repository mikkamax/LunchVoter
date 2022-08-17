package com.mike.lunchvoter.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "dish")
public class Dish {

    @EmbeddedId
    private DishIdentity dishIdentity;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

}
