package com.mike.lunchvoter.controller;

import com.mike.lunchvoter.dto.RestaurantDto;
import com.mike.lunchvoter.exception.IllegalRequestDataException;
import com.mike.lunchvoter.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(RestaurantController.RESTAURANT_URL)
public class RestaurantController {

    static final String RESTAURANT_URL = "/restaurants";

    private final RestaurantService restaurantService;
    //- рестораны
    //-- создавать
    //-- смотреть
    //-- редактировать название
    //-- активировать/деактивировать ресторан
    //-- смотреть весь список
    //-- смотреть список активных
    //-- искать по названию ресторана

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
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
                                              @Nullable @RequestParam(required = false) Boolean isActive) {
        if (name == null && address == null && isActive == null) {
            return restaurantService.getAll();
        }

        return restaurantService.getByParams(name, address, isActive);
    }

    @PostMapping("/{id}")
    public RestaurantDto update(@NotNull @PathVariable("id") Integer restaurantId,
                                @Valid @RequestBody RestaurantDto restaurantDto) {
        if (!Objects.equals(restaurantId, restaurantDto.getId())) {
            throw new IllegalRequestDataException(restaurantDto + " must be with id = " + restaurantId);
        }

        return restaurantService.update(restaurantId, restaurantDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@NotNull @PathVariable("id") Integer restaurantId) {
        restaurantService.delete(restaurantId);
    }
}
