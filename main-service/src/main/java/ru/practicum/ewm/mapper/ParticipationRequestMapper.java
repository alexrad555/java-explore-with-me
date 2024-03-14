package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.controller.dto.ParticipationRequestDto;
import ru.practicum.ewm.model.ParticipationRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipationRequestMapper {
    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto toDto(ParticipationRequest participationRequest);

    List<ParticipationRequestDto> toDto(List<ParticipationRequest> participationRequest);
}
