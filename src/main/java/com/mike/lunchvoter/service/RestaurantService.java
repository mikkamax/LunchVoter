package com.mike.lunchvoter.service;

import com.mike.lunchvoter.dto.RestaurantDto;
import com.mike.lunchvoter.entity.Restaurant;
import com.mike.lunchvoter.exception.CustomConstraintViolationException;
import com.mike.lunchvoter.exception.ObjectNotFoundException;
import com.mike.lunchvoter.mapping.RestaurantMapper;
import com.mike.lunchvoter.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

    private static final ExampleMatcher EXAMPLE_MATCHER_FOR_GET_BY_PARAMS = ExampleMatcher.matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase()
            .withIgnoreNullValues();
    private static final int NON_EXISTING_ID = -1;

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository,
                             RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Transactional
    public RestaurantDto create(RestaurantDto restaurantDto) {
        return createOrUpdate(restaurantDto);
    }

    public RestaurantDto get(Integer restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(restaurantMapper::mapToDto)
                .orElseThrow(() -> objectNotFoundException(restaurantId));
    }

    public List<RestaurantDto> getAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::mapToDto)
                .toList();
    }

    public List<RestaurantDto> getByParams(String name, String address, Boolean isActive) {
        Example<Restaurant> example = Example.of(
                new Restaurant()
                        .setName(name)
                        .setAddress(address)
                        .setIsActive(isActive),
                EXAMPLE_MATCHER_FOR_GET_BY_PARAMS
        );

        return restaurantRepository.findAll(example)
                .stream()
                .map(restaurantMapper::mapToDto)
                .toList();
    }

    @Transactional
    public RestaurantDto update(Integer restaurantId, RestaurantDto restaurantDto) {
        checkIfRestaurantWithThisIdExistsOrElseThrow(restaurantId);
        return createOrUpdate(restaurantDto);
    }

    @Transactional
    public void delete(Integer restaurantId) {
        checkIfRestaurantWithThisIdExistsOrElseThrow(restaurantId);
        restaurantRepository.deleteById(restaurantId);
    }

    private RestaurantDto createOrUpdate(RestaurantDto restaurantDto) {
        Restaurant restaurant = restaurantMapper.mapToEntity(restaurantDto);

        checkForNoConstraintViolationsOrElseThrow(restaurant);

        return restaurantMapper.mapToDto(
                restaurantRepository.save(restaurant)
        );
    }

    private void checkIfRestaurantWithThisIdExistsOrElseThrow(Integer restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw objectNotFoundException(restaurantId);
        }
    }

    private void checkForNoConstraintViolationsOrElseThrow(Restaurant restaurant) {
        boolean isConstraintViolated = restaurantRepository.existsByIdNotAndNameEqualsIgnoreCaseAndAddressEqualsIgnoreCase(
                Optional.ofNullable(restaurant.getId()).orElse(NON_EXISTING_ID),
                restaurant.getName(),
                restaurant.getAddress()
        );

        if (isConstraintViolated) {
            throw new CustomConstraintViolationException("Cannot save " + restaurant
                    + " because Restaurant with same name and address already exists in the database");
        }
    }

    private static ObjectNotFoundException objectNotFoundException(Integer restaurantId) {
        return new ObjectNotFoundException("There is no restaurant with id = " + restaurantId);
    }

}
