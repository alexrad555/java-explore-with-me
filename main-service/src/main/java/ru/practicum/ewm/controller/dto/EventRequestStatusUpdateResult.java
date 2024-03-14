package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Getter
public class EventRequestStatusUpdateResult {

    private final List<ParticipationRequestDto> confirmedRequests;

    private final List<ParticipationRequestDto> rejectedRequests;
}
