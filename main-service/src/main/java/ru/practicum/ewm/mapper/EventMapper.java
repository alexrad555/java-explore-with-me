package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.controller.dto.*;
import ru.practicum.ewm.model.Event;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CategoryMapper.class})
public interface EventMapper {
    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "lon", source = "location.lon")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "paid", source = "paid", defaultValue = "false")
    @Mapping(target = "participantLimit", source = "participantLimit", defaultValue = "0")
    @Mapping(target = "requestModeration", source = "requestModeration", defaultValue = "true")
    Event toEvent(NewEventDto newEventDto);
    UpdateEventRequest toRequest(UpdateEventAdminRequest adminRequest);
    UpdateEventRequest toRequest(UpdateEventUserRequest userRequest);

    @Mapping(target = "initiator", source = "user")
    @Mapping(target = "location.lat", source = "lat")
    @Mapping(target = "location.lon", source = "lon")
    @Mapping(target = "createdOn", source = "createDate")
    @Mapping(target = "confirmedRequests", constant = "0L")
    @Mapping(target = "state", source = "status")
    EventFullDto toFullDto(Event event);

    List<EventShortDto> toShortDto(List<EventFullDto> event);

}
