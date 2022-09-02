package com.mike.lunchvoter.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mike.lunchvoter.validation.ValidateOnCreate;
import com.mike.lunchvoter.validation.ValidateOnUpdate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantDto {

    /**
     * ID of the restaurant
     */
    @Null(groups = ValidateOnCreate.class)
    @NotNull(groups = ValidateOnUpdate.class)
    private Long id;

    /**
     * Name of the restaurant
     */
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    /**
     * Address of the restaurant
     */
    @NotBlank
    @Size(min = 5, max = 255)
    private String address;

    /**
     * List of corresponding menus
     */
    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<MenuDto> menus;

}
