package com.mike.lunchvoter.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
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
    private Integer id;

    /**
     * Name of the restaurant
     */
    @NotBlank
    @Size(min = 2, max = 100)
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
