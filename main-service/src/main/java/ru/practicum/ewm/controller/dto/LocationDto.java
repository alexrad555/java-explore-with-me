package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Getter
public class LocationDto {

    private BigDecimal lat;

    private BigDecimal lon;
}
