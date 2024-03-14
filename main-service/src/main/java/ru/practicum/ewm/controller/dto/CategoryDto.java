package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Getter
public class CategoryDto {

    private Long id;

    private String name;
}
