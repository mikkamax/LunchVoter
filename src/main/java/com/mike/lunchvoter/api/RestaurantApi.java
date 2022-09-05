package com.mike.lunchvoter.api;

import com.mike.lunchvoter.dto.RestaurantDto;
import com.mike.lunchvoter.dto.VoteDto;
import com.mike.lunchvoter.validation.ValidateOnCreate;
import com.mike.lunchvoter.validation.ValidateOnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequestMapping(value = RestaurantApi.RESTAURANT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Restaurant API", description = "Restaurant management")
@Validated
public interface RestaurantApi {

    String RESTAURANT_URL = "/api/v1/restaurants";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(ValidateOnCreate.class)
    @PreAuthorize("hasAuthority('restaurant:create')")
    @SecurityRequirement(name = "admin")
    @Operation(
            summary = "Add a new restaurant",
            description = """
                    Returns created restaurant without any menus. Requires *restaurant:create* permission.
                                        
                    Excluded (may provide null value as well) properties:
                    - id
                    """
    )
    RestaurantDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "name": "Ample Hills Creamery Chelsea",
                                                "address": "141 8th Ave"
                                            }"""
                            )
                    )
            )
            @Valid @RequestBody RestaurantDto restaurantDto);

    @GetMapping("/{restaurantId}")
    @Operation(
            summary = "Get restaurant by ID",
            description = "Returns single restaurant without any menus. No permission required."
    )
    RestaurantDto get(@Parameter(description = "ID of restaurant to return", required = true, example = "1")
                      @NotNull @PathVariable Long restaurantId);

    @GetMapping
    @Operation(
            summary = "Get restaurants by params",
            description = "Returns list of restaurants without any menus containing requested params. " +
                    "If no params provided - returns list of all restaurants. No permission required."
    )
    List<RestaurantDto> getAllByParams(@Nullable @RequestParam(required = false) String name,
                                       @Nullable @RequestParam(required = false) String address);

    @GetMapping("/today")
    @Operation(
            summary = "Get restaurants which have menu for today",
            description = "Returns list of restaurants with today's menus. No permission required."
    )
    List<RestaurantDto> getAllWithMenusForToday();

    @PostMapping("/{restaurantId}/votes")
    @PreAuthorize("hasAuthority('restaurant:vote')")
    @SecurityRequirement(name = "user")
    @Operation(
            summary = "Vote for restaurant",
            description = "Authenticated user can give his or her today's vote for restaurant." +
                    " Returns created vote. Requires *restaurant:vote* permission."
    )
    VoteDto voteForRestaurant(@Parameter(description = "ID of restaurant to vote for", required = true, example = "1")
                              @NotNull @PathVariable Long restaurantId,
                              @NotNull Authentication authentication);

    @PutMapping("/{restaurantId}")
    @Validated(ValidateOnUpdate.class)
    @PreAuthorize("hasAuthority('restaurant:update')")
    @SecurityRequirement(name = "admin")
    @Operation(
            summary = "Update an existing restaurant",
            description = "Returns updated restaurant without any menus. Requires *restaurant:update* permission."
    )
    RestaurantDto update(@Parameter(description = "ID of restaurant to update, should match ID in request body", required = true, example = "1")
                         @NotNull @PathVariable Long restaurantId,
                         @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                 content = @Content(
                                         examples = @ExampleObject(
                                                 value = """
                                                         {
                                                            "id": 1,
                                                            "name": "Old Bagel Restaurant",
                                                            "address": "111 8th Avenue"
                                                         }"""
                                         )
                                 )
                         )
                         @Valid @RequestBody RestaurantDto restaurantDto);

    @DeleteMapping("/{restaurantId}")
    @PreAuthorize("hasAuthority('restaurant:delete')")
    @SecurityRequirement(name = "admin")
    @Operation(
            summary = "Delete restaurant by ID",
            description = "Returns nothing if restaurant successfully deleted. Requires *restaurant:delete* permission."
    )
    void delete(@Parameter(description = "ID of restaurant to delete", required = true, example = "1")
                @NotNull @PathVariable Long restaurantId);

}
