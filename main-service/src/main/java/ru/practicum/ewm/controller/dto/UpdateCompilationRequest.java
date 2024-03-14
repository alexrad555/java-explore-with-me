package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Getter
public class UpdateCompilationRequest {

    private final List<Long> events;

    private final Boolean pinned;

    @Size(min = 1, max = 50)
    private final String title;
}
