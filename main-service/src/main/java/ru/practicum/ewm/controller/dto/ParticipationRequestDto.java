package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.ewm.model.ParticipationRequestStatus;

import java.time.LocalDateTime;

@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Getter
public class ParticipationRequestDto {

    private final LocalDateTime created;

    private final Long event;

    private final Long id;

    private final Long requester;

    private final ParticipationRequestStatus status;
}

