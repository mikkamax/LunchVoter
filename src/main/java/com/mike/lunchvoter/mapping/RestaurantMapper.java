package com.mike.lunchvoter.mapping;

import com.mike.lunchvoter.dto.RestaurantDto;
import com.mike.lunchvoter.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RestaurantMapper {

    @Mapping(target = "isActive", source = "isActive", defaultValue = "true")
    Restaurant restaurantDtoToRestaurant(RestaurantDto restaurantDto);

    RestaurantDto restaurantToRestaurantDto(Restaurant restaurant);

}
