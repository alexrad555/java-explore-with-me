package ru.practicum.ewm.controller.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UpdateUserCommentDto {
    @Size(min = 1, max = 7000)
    @NotBlank
    private String message;
}
