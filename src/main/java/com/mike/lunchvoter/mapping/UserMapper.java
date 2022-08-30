package com.mike.lunchvoter.mapping;

import com.mike.lunchvoter.dto.UserDto;
import com.mike.lunchvoter.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User mapToEntity(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    User mapToEntityOnUpdate(UserDto userDto, @MappingTarget User user);

    UserDto mapToDto(User user);

}
