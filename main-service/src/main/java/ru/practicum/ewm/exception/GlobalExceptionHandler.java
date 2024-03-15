package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.dto.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDataNotFoundException(final DataNotFoundException exception) {
        log.error("Данные не найдены {}", exception.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerDuplicateException(final DuplicateException exception) {
        log.error("содержит дубликат {}", exception.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(EventTimeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerEventTimeException(final EventTimeException exception) {
        log.error("неправильное время {}", exception.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerConstraintViolationException(final ConstraintViolationException exception) {
        log.error("невозможное действие {}", exception.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(InvalidEventActionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerInvalidEventActionException(final InvalidEventActionException exception) {
        log.error("невозможное действие {}", exception.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(RequestValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerRequestValidationException(final RequestValidationException exception) {
        log.error("ошибка валидации запроса на участие", exception);
        return new ErrorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(InvalidEventStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerInvalidEventStateException(final InvalidEventStateException exception) {
        log.error("ошибка валидации запроса на участие", exception);
        return new ErrorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException exception) {
        log.error("Ошибка входящих данных {}", exception.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException exception) {
        log.error("содержит дубликат {}", exception.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }
}
