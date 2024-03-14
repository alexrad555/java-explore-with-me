package ru.practicum.ewm.exception;

public class InvalidEventActionException extends RuntimeException {
    public InvalidEventActionException(String message) {
        super(message);
    }
}
