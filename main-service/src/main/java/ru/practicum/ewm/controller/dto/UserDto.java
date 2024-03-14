package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Getter
public class UserDto {

    private final Long id;

    private final String name;

    private final String email;
}
