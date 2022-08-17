package com.mike.lunchvoter.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@Data
public class DishIdentity implements Serializable {

    @NotNull
    @Column(name = "menu_id")
    private Integer menuId;

    @NotNull
    @Column(name = "name")
    private String name;

}
