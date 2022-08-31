package com.mike.lunchvoter.controller;

import com.mike.lunchvoter.dto.RestaurantDto;
import com.mike.lunchvoter.exception.IllegalRequestDataException;
import com.mike.lunchvoter.service.RestaurantService;
import com.mike.lunchvoter.validation.ValidateOnCreate;
import com.mike.lunchvoter.validation.ValidateOnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public RestaurantDto get(@NotNull @PathVariable("id") Integer restaurantId) {
        return restaurantService.get(restaurantId);
    }

    @GetMapping
    public List<RestaurantDto> getAllByParams(@Nullable @RequestParam(required = false) String name,
                                              @Nullable @RequestParam(required = false) String address,
                                              @Nullable @RequestParam(required = false) Boolean enabled) {
        if (name == null && address == null && enabled == null) {
            return restaurantService.getAll();
        }

        return restaurantService.getByParams(name, address, enabled);
    }

    @PutMapping("/{id}")
    @Validated(ValidateOnUpdate.class)
    @PreAuthorize("hasAuthority('restaurant:update')")
    public RestaurantDto update(@NotNull @PathVariable("id") Integer restaurantId,
                                @Valid @RequestBody RestaurantDto restaurantDto) {
        if (!Objects.equals(restaurantId, restaurantDto.getId())) {
            throw new IllegalRequestDataException(restaurantDto + " id doesn't match path id = " + restaurantId);
        }

        return restaurantService.update(restaurantId, restaurantDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('restaurant:delete')")
    public void delete(@NotNull @PathVariable("id") Integer restaurantId) {
        restaurantService.delete(restaurantId);
    }
}
