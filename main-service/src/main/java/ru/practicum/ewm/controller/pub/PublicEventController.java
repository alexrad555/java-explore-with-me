package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controller.dto.EventFullDto;
import ru.practicum.ewm.controller.dto.EventShortDto;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.EventSearchParam;
import ru.practicum.ewm.service.EventService;
import ru.practicum.stats.client.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@RestController
@RequestMapping("events")
public class PublicEventController {

    private final StatClient statClient;
    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public List<EventShortDto> findAll(@RequestParam(required = false) String text,
                                       @RequestParam(required = false) List<Long> categories,
                                       @RequestParam(required = false) Boolean paid,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                       @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(required = false, defaultValue = "0") int from,
                                       @RequestParam(required = false, defaultValue = "10") int size,
                                       HttpServletRequest request) {
        statClient.createHit(request);
        EventSearchParam eventSearchParam = new EventSearchParam(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, true);

        return eventMapper.toShortDto(eventService.findAll(eventSearchParam));
    }

    @GetMapping("{eventId}")
    public EventFullDto findOne(@PathVariable Long eventId, HttpServletRequest request) {
        statClient.createHit(request);
        return eventService.findEventDtoById(eventId, true);
    }
}
