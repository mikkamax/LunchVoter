package com.mike.lunchvoter.service;

import com.mike.lunchvoter.dto.MenuDto;
import com.mike.lunchvoter.entity.Dish;
import com.mike.lunchvoter.entity.Menu;
import com.mike.lunchvoter.exception.CustomConstraintViolationException;
import com.mike.lunchvoter.exception.ObjectNotFoundException;
import com.mike.lunchvoter.mapping.MenuMapper;
import com.mike.lunchvoter.repository.DishRepository;
import com.mike.lunchvoter.repository.MenuRepository;
import com.mike.lunchvoter.repository.RestaurantRepository;
import com.mike.lunchvoter.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private static final ExampleMatcher EXAMPLE_MATCHER_FOR_GET_BY_PARAMS = ExampleMatcher.matching()
            .withIgnoreNullValues();

    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuMapper menuMapper;

    @Autowired
    public MenuService(MenuRepository menuRepository,
                       DishRepository dishRepository,
                       RestaurantRepository restaurantRepository,
                       MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuMapper = menuMapper;
    }

    @Transactional
    public MenuDto create(MenuDto menuDto) {
        checkIfReferencedRestaurantExistsOrElseThrow(menuDto);
        checkForNoConstraintViolationsOrElseThrow(menuDto);

        Menu menu = menuMapper.mapToEntity(menuDto);

        return menuMapper.mapToDto(
                menuRepository.save(menu)
        );
    }

    public MenuDto get(Long menuId) {
        return menuRepository.findById(menuId)
                .map(menuMapper::mapToDto)
                .orElseThrow(() -> objectNotFoundException(menuId));
    }

    public List<MenuDto> getAll() {
        return menuRepository.findAll()
                .stream()
                .map(menuMapper::mapToDto)
                .toList();
    }

    public List<MenuDto> getAllByParams(LocalDate date, Long restaurantId) {
        Example<Menu> example = Example.of(
                new Menu()
                        .setDate(date)
                        .setRestaurantId(restaurantId),
                EXAMPLE_MATCHER_FOR_GET_BY_PARAMS
        );

        return menuRepository.findAll(example)
                .stream()
                .map(menuMapper::mapToDto)
                .toList();
    }

    @Transactional
    public MenuDto update(Long menuId, MenuDto menuDto) {
        checkIfMenuWithThisIdExistsOrElseThrow(menuId);
        checkIfReferencedRestaurantExistsOrElseThrow(menuDto);
        checkForNoConstraintViolationsOrElseThrow(menuDto);

        Menu menu = menuMapper.mapToEntity(menuDto);
        checkConsistencyForAllDishesWithIdsOrElseThrow(menu);

        return menuMapper.mapToDto(
                menuRepository.save(menu)
        );
    }

    @Transactional
    public void delete(Long menuId) {
        checkIfMenuWithThisIdExistsOrElseThrow(menuId);
        menuRepository.deleteById(menuId);
    }

    private void checkIfReferencedRestaurantExistsOrElseThrow(MenuDto menuDto) {
        if (!restaurantRepository.existsById(menuDto.getRestaurantId())) {
            throw new CustomConstraintViolationException("Cannot save " + menuDto
                    + " because restaurantId with Id = " + menuDto.getRestaurantId() + " doesn't exist");
        }
    }

    private void checkForNoConstraintViolationsOrElseThrow(MenuDto menuDto) {
        boolean isConstraintViolated = menuRepository.existsByIdNotAndRestaurantIdAndDate(
                Optional.ofNullable(menuDto.getId()).orElse(Constants.NON_EXISTING_ID),
                menuDto.getRestaurantId(),
                menuDto.getDate()
        );

        if (isConstraintViolated) {
            throw new CustomConstraintViolationException("Cannot save " + menuDto
                    + " because Menu with same restaurantId and date already exists in the database");
        }
    }

    private void checkIfMenuWithThisIdExistsOrElseThrow(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw objectNotFoundException(menuId);
        }
    }

    private void checkConsistencyForAllDishesWithIdsOrElseThrow(Menu menu) {
        List<Dish> dishes = menu.getDishes();

        if (dishes == null || dishes.isEmpty()) {
            return;
        }

        dishes.stream()
                .filter(dish -> Objects.nonNull(dish.getId()))
                .forEach(dish -> {
                    if (!dishRepository.existsByIdAndMenu_Id(dish.getId(), menu.getId())) {
                        throw new ObjectNotFoundException("Dish with id = " + dish.getId()
                                + " doesn't exist or do not belong to menu with id = " + menu.getId());
                    }
                });
    }

    private static ObjectNotFoundException objectNotFoundException(Long menuId) {
        return new ObjectNotFoundException("There is no menu with id = " + menuId);
    }

}
