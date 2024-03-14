package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controller.dto.EventFullDto;
import ru.practicum.ewm.controller.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.model.EventSearchParam;
import ru.practicum.ewm.model.EventStatus;
import ru.practicum.ewm.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@RestController
@RequestMapping("admin/events")
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> findAll(@RequestParam(required = false) List<Long> users,
                                      @RequestParam(required = false) List<EventStatus> states,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                      @RequestParam(required = false, defaultValue = "0") int from,
                                      @RequestParam(required = false, defaultValue = "10") int size) {
        EventSearchParam eventSearchParam = new EventSearchParam(categories, rangeStart, rangeEnd, from, size, users, states, false);
        return eventService.findAll(eventSearchParam);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventAdminRequest requestDto) {
        return eventService.update(eventId, requestDto);
    }

}
