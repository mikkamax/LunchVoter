package com.mike.lunchvoter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoteDto {

    /**
     * User's id
     */
    @NotNull
    private Integer userId;

    /**
     * Vote's date
     */
    @NotNull
    private LocalDate date;

    /**
     * Restaurant's id
     */
    @NotNull
    private Integer restaurantId;

}
