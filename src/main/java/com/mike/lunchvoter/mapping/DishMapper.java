package com.mike.lunchvoter.mapping;

import com.mike.lunchvoter.dto.DishDto;
import com.mike.lunchvoter.entity.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DishMapper {

    Dish mapToEntity(DishDto dishDto);

    DishDto mapToDto(Dish dish);

}
