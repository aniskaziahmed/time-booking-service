package com.fantasy.tbs.service.mapper;

import com.fantasy.tbs.domain.TimeBookDTO;
import com.fantasy.tbs.domain.TimeBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//Used componentModel to include @Component annotation in the impl to handle DI
@Mapper(componentModel = "spring")
public interface TimeBookMapper {
    @Mapping(target = "booking", source = "timeStamp")
    TimeBooking toTimeBooking(TimeBookDTO timeBookDTO);
}
