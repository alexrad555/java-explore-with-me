package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Getter
@Setter
public class EventShortDto {

    private final CategoryDto category;

    private final String annotation;

    private final Long confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final  LocalDateTime eventDate;

    private final Long id;

    private final UserShortDto initiator;

    private final Boolean paid;

    private final String title;

    private long views;

    @JsonIgnore
    private final LocalDateTime createDate;
}