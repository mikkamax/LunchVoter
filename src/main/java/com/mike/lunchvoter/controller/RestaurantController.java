package com.mike.lunchvoter.controller;

import com.mike.lunchvoter.dto.RestaurantDto;
import com.mike.lunchvoter.dto.VoteDto;
import com.mike.lunchvoter.exception.IllegalRequestDataException;
import com.mike.lunchvoter.service.RestaurantService;
import com.mike.lunchvoter.util.SecurityUtil;
import com.mike.lunchvoter.validation.ValidateOnCreate;
import com.mike.lunchvoter.validation.ValidateOnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(RestaurantController.RESTAURANT_URL)
@Validated
public class RestaurantController {

    static final String RESTAURANT_URL = "/api/v1/restaurants";

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(ValidateOnCreate.class)
    @PreAuthorize("hasAuthority('restaurant:create')")
    public RestaurantDto create(@Valid @RequestBody RestaurantDto restaurantDto) {
        return restaurantService.create(restaurantDto);
    }

    @GetMapping("/{id}")
    public RestaurantDto get(@NotNull @PathVariable("id") Long restaurantId) {
        return restaurantService.get(restaurantId);
    }

    @GetMapping
    public List<RestaurantDto> getAllByParams(@Nullable @RequestParam(required = false) String name,
                                              @Nullable @RequestParam(required = false) String address) {
        if (name == null && address == null) {
            return restaurantService.getAll();
        }

        return restaurantService.getByParams(name, address);
    }

    @GetMapping("/today")
    @Cacheable("restaurantsToday")
    public List<RestaurantDto> getAllWithMenusForToday() {
        return restaurantService.getAllWithMenusForToday();
    }

    @PostMapping("/{id}/votes")
    @PreAuthorize("hasAuthority('restaurant:vote')")
    public VoteDto voteForRestaurant(@NotNull @PathVariable("id") Long restaurantId,
                                     @NotNull Authentication authentication) {
        Long userId = SecurityUtil.getAuthenticatedUserIdOrElseThrow(authentication);

        return restaurantService.voteForRestaurant(restaurantId, userId);
    }

    @PutMapping("/{id}")
    @Validated(ValidateOnUpdate.class)
    @PreAuthorize("hasAuthority('restaurant:update')")
    public RestaurantDto update(@NotNull @PathVariable("id") Long restaurantId,
                                @Valid @RequestBody RestaurantDto restaurantDto) {
        if (!Objects.equals(restaurantId, restaurantDto.getId())) {
            throw new IllegalRequestDataException(restaurantDto + " id doesn't match path id = " + restaurantId);
        }

        return restaurantService.update(restaurantId, restaurantDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('restaurant:delete')")
    public void delete(@NotNull @PathVariable("id") Long restaurantId) {
        restaurantService.delete(restaurantId);
    }

}
