package ru.practicum.stats.server.controller.mapper;

import org.mapstruct.Mapper;
import ru.practicum.stats.dto.StatResponse;
import ru.practicum.stats.server.report.Statistics;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatMapper {

    StatResponse toResponse(Statistics statistics);

    List<StatResponse> toResponse(List<Statistics> statistics);
}
