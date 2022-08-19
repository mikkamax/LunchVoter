package com.mike.lunchvoter.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.mike.lunchvoter.validation.ValidateOnCreate;
import com.mike.lunchvoter.validation.ValidateOnUpdate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class RestaurantDto {

    /**
     * ID of the restaurant
     */
    @Null(groups = ValidateOnCreate.class)
    @NotNull(groups = ValidateOnUpdate.class)
    private Integer id;

    /**
     * Name of the restaurant
     */
    @NotBlank
    @Size(max = 100)
    private String name;

    /**
     * Address of the restaurant
     */
    @NotBlank
    @Size(max = 255)
    private String address;

    /**
     * Is the restaurant active
     */
    private Boolean isActive;

}
