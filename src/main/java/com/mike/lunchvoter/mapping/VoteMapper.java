package com.mike.lunchvoter.mapping;

import com.mike.lunchvoter.dto.VoteDto;
import com.mike.lunchvoter.entity.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VoteMapper {

    @Mapping(target = "userId", source = "voteIdentity.userId")
    @Mapping(target = "date", source = "voteIdentity.date")
    VoteDto mapToDto(Vote vote);

}
