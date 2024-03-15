package ru.practicum.ewm.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.EventAction;

@Component
public class EventActionConverter implements Converter<String, EventAction> {

    @Override
    public EventAction convert(String source) {
        try {
            return EventAction.valueOf(source);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: " + source);
        }
    }
}
