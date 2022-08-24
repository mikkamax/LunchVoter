package com.mike.lunchvoter.mapping;

import com.mike.lunchvoter.dto.MenuDto;
import com.mike.lunchvoter.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = DishMapper.class)
public interface MenuMapper {

    Menu mapToEntity(MenuDto menuDto);

    MenuDto mapToDto(Menu menu);

}
