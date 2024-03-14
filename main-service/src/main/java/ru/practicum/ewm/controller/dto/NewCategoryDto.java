package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Getter
public class NewCategoryDto {

    @NotBlank
    @Size(min = 1, max = 50)
    private final String name;
}
