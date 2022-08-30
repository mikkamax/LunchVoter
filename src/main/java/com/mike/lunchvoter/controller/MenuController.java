package com.mike.lunchvoter.controller;

import com.mike.lunchvoter.dto.MenuDto;
import com.mike.lunchvoter.exception.IllegalRequestDataException;
import com.mike.lunchvoter.service.MenuService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(MenuController.MENU_URL)
@Validated
public class MenuController {

    static final String MENU_URL = "/api/v1/menus";

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(ValidateOnCreate.class)
    @PreAuthorize("hasAuthority('menu:create')")
    public MenuDto create(@Valid @RequestBody MenuDto menuDto) {
        return menuService.create(menuDto);
    }

    @GetMapping("/{id}")
    public MenuDto get(@NotNull @PathVariable("id") Integer menuId) {
        return menuService.get(menuId);
    }

    @GetMapping
    public List<MenuDto> getAllByParams(@Nullable @RequestParam(required = false) LocalDate date,
                                        @Nullable @RequestParam(required = false) Integer restaurantId) {
        if (date == null && restaurantId == null) {
            return menuService.getAll();
        }

        return menuService.getAllByParams(date, restaurantId);
    }

    @PostMapping("/{id}")
    @Validated(ValidateOnUpdate.class)
    @PreAuthorize("hasAuthority('menu:update')")
    public MenuDto update(@NotNull @PathVariable("id") Integer menuId,
                          @Valid @RequestBody MenuDto menuDto) {
        if (!Objects.equals(menuId, menuDto.getId())) {
            throw new IllegalRequestDataException(menuDto + " id doesn't match path id = " + menuId);
        }

        return menuService.update(menuId, menuDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('menu:delete')")
    public void delete(@NotNull @PathVariable("id") Integer menuId) {
        menuService.delete(menuId);
    }

}
