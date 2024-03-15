package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    private final String annotation;

    @NotNull
    private final Long category;

    @NotBlank
    @Size(min = 20, max = 7000)
    private final String description;

    @NotNull
    @Future
    private final LocalDateTime eventDate;

    @NotNull
    private final LocationDto location;

    private final Boolean paid;

    @PositiveOrZero
    private final Integer participantLimit;

    private final Boolean requestModeration;

    @NotBlank
    @Size(min = 3, max = 120)
    private final String title;

    @JsonCreator
    public NewEventDto(String annotation,
                       Long category,
                       String description,
                       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime eventDate,
                       LocationDto location,
                       Boolean paid,
                       Integer participantLimit,
                       Boolean requestModeration,
                       String title) {
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.title = title;
    }
}
