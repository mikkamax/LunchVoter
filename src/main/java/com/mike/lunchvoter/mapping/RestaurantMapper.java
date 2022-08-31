package com.mike.lunchvoter.mapping;

import com.mike.lunchvoter.dto.RestaurantDto;
import com.mike.lunchvoter.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = MenuMapper.class)
public interface RestaurantMapper {

    @Mapping(target = "menus", ignore = true)
    Restaurant mapToEntityWithoutMenus(RestaurantDto restaurantDto);

    @Mapping(target = "menus", ignore = true)
    RestaurantDto mapToDtoWithoutMenus(Restaurant restaurant);

    RestaurantDto mapToDtoWithMenus(Restaurant restaurant);

}
