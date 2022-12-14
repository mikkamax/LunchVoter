package com.mike.lunchvoter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mike.lunchvoter.validation.ValidateOnCreate;
import com.mike.lunchvoter.validation.ValidateOnUpdate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuDto {

    /**
     * ID of the menu
     */
    @Null(groups = ValidateOnCreate.class)
    @NotNull(groups = ValidateOnUpdate.class)
    private Long id;

    /**
     * ID of the corresponding restaurant
     */
    @NotNull
    private Long restaurantId;

    /**
     * ID date of the menu
     */
    @NotNull
    private LocalDate date;

    /**
     * List of corresponding dishes
     */
    @Nullable
    @Valid
    private List<DishDto> dishes;

}
