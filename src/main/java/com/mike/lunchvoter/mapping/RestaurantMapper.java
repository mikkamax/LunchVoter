package com.mike.lunchvoter.mapping;

import com.mike.lunchvoter.dto.RestaurantDto;
import com.mike.lunchvoter.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RestaurantMapper {

    Restaurant mapToEntity(RestaurantDto restaurantDto);

    RestaurantDto mapToDto(Restaurant restaurant);

}
