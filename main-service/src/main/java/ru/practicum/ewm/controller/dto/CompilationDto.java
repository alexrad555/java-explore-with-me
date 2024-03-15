package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class CompilationDto {

    private final Long id;

    private final List<EventShortDto> events;

    private final boolean pinned;

    private final String title;
}