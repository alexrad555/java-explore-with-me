package ru.practicum.ewm.controller.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class NewCommentDto {
    @NotBlank
    private String message;
}
