package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import ru.practicum.ewm.model.EventAction;

import java.time.LocalDateTime;

@Getter
public class UpdateEventRequest {
    private final String annotation;

    private final Long category;

    private final String description;

    private final LocalDateTime eventDate;

    private final LocationDto location;

    private final Boolean paid;

    private final Integer participantLimit;

    private final Boolean requestModeration;

    private final EventAction stateAction;

    private final String title;

    @JsonCreator
    public UpdateEventRequest(String annotation,
                              Long category,
                              String description,
                              @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime eventDate,
                              LocationDto location,
                              Boolean paid,
                              Integer participantLimit,
                              Boolean requestModeration,
                              EventAction stateAction,
                              String title) {
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.stateAction = stateAction;
        this.title = title;
    }
}
