package ru.practicum.ewm.exception.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Getter
public class ErrorResponse {

    private String status;
    private String reason;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(HttpStatus httpStatus, String message) {
        this.status = httpStatus.toString();
        this.reason = httpStatus.getReasonPhrase();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}