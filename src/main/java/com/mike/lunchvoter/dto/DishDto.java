package com.mike.lunchvoter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mike.lunchvoter.validation.ValidateOnCreate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DishDto {

    /**
     * ID of the dish
     */
    @Null(groups = ValidateOnCreate.class)
    private Long id;

    /**
     * Dish name
     */
    @NotBlank
    @Size(min = 2, max = 255)
    private String name;

    /**
     * Dish price
     */
    @NotNull
    private BigDecimal price;

}
