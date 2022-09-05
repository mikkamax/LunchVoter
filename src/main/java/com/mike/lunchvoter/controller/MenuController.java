package com.mike.lunchvoter.controller;

import com.mike.lunchvoter.api.MenuApi;
import com.mike.lunchvoter.dto.MenuDto;
import com.mike.lunchvoter.exception.IllegalRequestDataException;
import com.mike.lunchvoter.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
public class MenuController implements MenuApi {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    @CacheEvict(value = {"restaurantsToday", "menusToday"}, allEntries = true)
    public MenuDto create(@Valid @RequestBody MenuDto menuDto) {
        return menuService.create(menuDto);
    }

    @Override
    public MenuDto get(@NotNull @PathVariable Long menuId) {
        return menuService.get(menuId);
    }

    @Override
    public List<MenuDto> getAllByParams(@Nullable @RequestParam(required = false) LocalDate date,
                                        @Nullable @RequestParam(required = false) Long restaurantId) {
        if (date == null && restaurantId == null) {
            return menuService.getAll();
        }

        return menuService.getAllByParams(date, restaurantId);
    }

    @Override
    @Cacheable("menusToday")
    public List<MenuDto> getAllForToday() {
        return menuService.getAllByParams(LocalDate.now(), null);
    }

    @Override
    @CacheEvict(value = {"restaurantsToday", "menusToday"}, allEntries = true)
    public MenuDto update(@NotNull @PathVariable Long menuId,
                          @Valid @RequestBody MenuDto menuDto) {
        if (!Objects.equals(menuId, menuDto.getId())) {
            throw new IllegalRequestDataException(menuDto + " id doesn't match path id = " + menuId);
        }

        return menuService.update(menuId, menuDto);
    }

    @Override
    @CacheEvict(value = {"restaurantsToday", "menusToday"}, allEntries = true)
    public void delete(@NotNull @PathVariable Long menuId) {
        menuService.delete(menuId);
    }

}
