package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import ru.practicum.ewm.model.EventAction;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private final String annotation;

    private final Long category;

    @Size(min = 20, max = 7000)
    private final String description;

    @Future
    private final LocalDateTime eventDate;

    private final LocationDto location;

    private final Boolean paid;

    @Positive
    private final Integer participantLimit;

    private final Boolean requestModeration;

    private final EventAction stateAction;

    @Size(min = 3, max = 120)
    private final String title;

    @JsonCreator
    public UpdateEventAdminRequest(String annotation,
                                   Long category,
                                   String description,
                                   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime eventDate,
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
