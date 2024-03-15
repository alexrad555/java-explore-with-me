package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.model.EventStatus;

import java.time.LocalDateTime;

@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Getter
@Setter
@ToString
public class EventFullDto {
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdOn;

    private final LocationDto location;

    private final String description;

    private final Integer participantLimit;

    private final LocalDateTime publishedOn;

    private final boolean requestModeration;

    private final EventStatus state;
}

