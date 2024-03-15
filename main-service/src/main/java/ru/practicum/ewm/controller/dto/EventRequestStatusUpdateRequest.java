package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.model.ParticipationRequestStatus;

import java.util.List;

@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Getter
@Setter
public class EventRequestStatusUpdateRequest {

    private final List<Long> requestIds;

    private final ParticipationRequestStatus status;
}
