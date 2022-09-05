package com.mike.lunchvoter.api;

import com.mike.lunchvoter.dto.MenuDto;
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
import java.time.LocalDate;
import java.util.List;

@RequestMapping(value = MenuApi.MENU_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Menu API", description = "Menu management")
@Validated
public interface MenuApi {

    String MENU_URL = "/api/v1/menus";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(ValidateOnCreate.class)
    @PreAuthorize("hasAuthority('menu:create')")
    @SecurityRequirement(name = "admin")
    @Operation(
            summary = "Add a new menu",
            description = """
                    Returns created menu. Requires *menu:create* permission.
                                        
                    Excluded (may provide null value as well) properties:
                    - id
                                        
                    Optional properties:
                    - dishes
                    """
    )
    MenuDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "restaurantId": 1,
                                                "date": "2022-09-03",
                                                "dishes": [
                                                    {
                                                        "name": "Bagel",
                                                        "price": 25.5
                                                    },
                                                    {
                                                        "name": "Pumpkin soup",
                                                        "price": 50
                                                    },
                                                    {
                                                        "name": "Grilled beef",
                                                        "price": 85
                                                    }
                                                ]
                                            }"""
                            )
                    )
            )
            @Valid @RequestBody MenuDto menuDto);

    @GetMapping("/{menuId}")
    @Operation(
            summary = "Get menu by ID",
            description = "Returns single menu with dishes. No permission required."
    )
    MenuDto get(@Parameter(description = "ID of menu to return", required = true, example = "1")
                @NotNull @PathVariable Long menuId);

    @GetMapping
    @Operation(
            summary = "Get menus by params",
            description = "Returns list of today's menus with dishes containing requested params. " +
                    "If no params provided - returns list of all menus. No permission required."
    )
    List<MenuDto> getAllByParams(@Nullable @RequestParam(required = false) LocalDate date,
                                 @Nullable @RequestParam(required = false) Long restaurantId);

    @GetMapping("/today")
    @Operation(
            summary = "Get today's menus",
            description = "Returns list of menus with dishes. No permission required."
    )
    List<MenuDto> getAllForToday();

    @PutMapping("/{menuId}")
    @Validated(ValidateOnUpdate.class)
    @PreAuthorize("hasAuthority('menu:update')")
    @SecurityRequirement(name = "admin")
    @Operation(
            summary = "Update an existing menu",
            description = """
                    Returns updated menu with dishes. Requires *menu:update* permission.

                    Optional properties:
                    - dishes
                                        
                    Beware: the list of dishes on the menu is defined here. Any dishes that were previously on this menu,
                    but not included in the updated menu, will be removed from the database.
                    """
    )
    MenuDto update(@Parameter(description = "ID of menu to update, should match ID in request body", required = true, example = "1")
                   @NotNull @PathVariable Long menuId,
                   @io.swagger.v3.oas.annotations.parameters.RequestBody(
                           content = @Content(
                                   examples = @ExampleObject(
                                           value = """
                                                   {
                                                       "id": 1,
                                                       "restaurantId": 1,
                                                       "date": "2022-09-03",
                                                       "dishes": [
                                                           {
                                                               "id": 1,
                                                               "name": "Soup",
                                                               "price": 30
                                                           },
                                                           {
                                                               "id": 2,
                                                               "name": "Hamburger",
                                                               "price": 70.5
                                                           },
                                                       ]
                                                   }"""
                                   )
                           )
                   )
                   @Valid @RequestBody MenuDto menuDto);

    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasAuthority('menu:delete')")
    @SecurityRequirement(name = "admin")
    @Operation(
            summary = "Delete menu by ID",
            description = "Returns nothing if menu successfully deleted. Requires *menu:delete* permission."
    )
    void delete(@Parameter(description = "ID of menu to delete", required = true, example = "1")
                @NotNull @PathVariable Long menuId);

}
