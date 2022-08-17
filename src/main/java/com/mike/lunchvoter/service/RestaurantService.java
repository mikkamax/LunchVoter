package com.mike.lunchvoter.service;

import com.mike.lunchvoter.dto.RestaurantDto;
import com.mike.lunchvoter.entity.Restaurant;
import com.mike.lunchvoter.exception.ObjectNotFoundException;
import com.mike.lunchvoter.mapping.RestaurantMapper;
import com.mike.lunchvoter.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private static final ExampleMatcher EXAMPLE_MATCHER_FOR_GET_BY_PARAMS = ExampleMatcher.matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase()
            .withIgnoreNullValues();

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;


    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository,
                             RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }

    public RestaurantDto create(RestaurantDto restaurantDto) {
        return createOrUpdate(restaurantDto);
    }

    public RestaurantDto get(Integer restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(restaurantMapper::restaurantToRestaurantDto)
                .orElseThrow(() -> objectNotFoundException(restaurantId));
    }

    public List<RestaurantDto> getAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::restaurantToRestaurantDto)
                .toList();
    }

    public List<RestaurantDto> getByParams(String name, String address, Boolean isActive) {
        Example<Restaurant> exampleQuery = Example.of(
                new Restaurant()
                        .setName(name)
                        .setAddress(address)
                        .setIsActive(isActive),
                EXAMPLE_MATCHER_FOR_GET_BY_PARAMS
        );

        return restaurantRepository.findAll(exampleQuery)
                .stream()
                .map(restaurantMapper::restaurantToRestaurantDto)
                .toList();
    }

    public RestaurantDto update(Integer restaurantId, RestaurantDto restaurantDto) {
        checkIfExistOrElseThrow(restaurantId);
        return createOrUpdate(restaurantDto);
    }

    public void delete(Integer restaurantId) {
        checkIfExistOrElseThrow(restaurantId);
        restaurantRepository.deleteById(restaurantId);
    }

    private RestaurantDto createOrUpdate(RestaurantDto restaurantDto) {
        Restaurant restaurant = restaurantRepository.save(
                restaurantMapper.restaurantDtoToRestaurant(restaurantDto)
        );

        return restaurantMapper.restaurantToRestaurantDto(restaurant);
    }

    private void checkIfExistOrElseThrow(Integer restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw objectNotFoundException(restaurantId);
        }
    }

    private ObjectNotFoundException objectNotFoundException(Integer restaurantId) {
        return new ObjectNotFoundException("There is no restaurant with id = " + restaurantId);
    }

}
