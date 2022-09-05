package com.mike.lunchvoter.controller;

import com.mike.lunchvoter.api.RestaurantApi;
import com.mike.lunchvoter.dto.RestaurantDto;
import com.mike.lunchvoter.dto.VoteDto;
import com.mike.lunchvoter.exception.IllegalRequestDataException;
import com.mike.lunchvoter.service.RestaurantService;
import com.mike.lunchvoter.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@RestController
public class RestaurantController implements RestaurantApi {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    public RestaurantDto create(@Valid @RequestBody RestaurantDto restaurantDto) {
        return restaurantService.create(restaurantDto);
    }

    @Override
    public RestaurantDto get(@NotNull @PathVariable Long restaurantId) {
        return restaurantService.get(restaurantId);
    }

    @Override
    public List<RestaurantDto> getAllByParams(@Nullable @RequestParam(required = false) String name,
                                              @Nullable @RequestParam(required = false) String address) {
        if (name == null && address == null) {
            return restaurantService.getAll();
        }

        return restaurantService.getByParams(name, address);
    }

    @Override
    @Cacheable("restaurantsToday")
    public List<RestaurantDto> getAllWithMenusForToday() {
        return restaurantService.getAllWithMenusForToday();
    }

    @Override
    public VoteDto voteForRestaurant(@NotNull @PathVariable Long restaurantId,
                                     @NotNull Authentication authentication) {
        Long userId = SecurityUtil.getAuthenticatedUserIdOrElseThrow(authentication);

        return restaurantService.voteForRestaurant(restaurantId, userId);
    }

    @Override
    @CacheEvict(value = {"restaurantsToday", "menusToday"}, allEntries = true)
    public RestaurantDto update(@NotNull @PathVariable Long restaurantId,
                                @Valid @RequestBody RestaurantDto restaurantDto) {
        if (!Objects.equals(restaurantId, restaurantDto.getId())) {
            throw new IllegalRequestDataException(restaurantDto + " id doesn't match path id = " + restaurantId);
        }

        return restaurantService.update(restaurantId, restaurantDto);
    }

    @Override
    @CacheEvict(value = {"restaurantsToday", "menusToday"}, allEntries = true)
    public void delete(@NotNull @PathVariable Long restaurantId) {
        restaurantService.delete(restaurantId);
    }

}
