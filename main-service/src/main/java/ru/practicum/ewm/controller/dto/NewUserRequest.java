package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.*;

@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Getter
public class NewUserRequest {

    @NotBlank
    @Size(min = 2, max = 250)
    private final String name;

    @Email
    @NotBlank
    @Size(min = 6, max = 254)
    private final String email;
}
