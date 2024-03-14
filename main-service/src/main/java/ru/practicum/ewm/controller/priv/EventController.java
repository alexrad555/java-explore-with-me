package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controller.dto.*;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.model.EventSearchParam;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.ParticipationRequestService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@RestController
@RequestMapping("users/{userId}/events")
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final ParticipationRequestService requestService;
    private final ParticipationRequestMapper requestMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId,
                               @Valid @RequestBody NewEventDto eventDto) {
        return eventMapper.toFullDto(eventService.create(userId, eventDto));
    }

    @PatchMapping("{eventId}")
    public EventFullDto update(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventUserRequest updateDto) {
        return eventService.update(eventId, userId, updateDto);
    }

    @GetMapping
    public List<EventShortDto> findAll(@PathVariable Long userId,
                                       @RequestParam(required = false, defaultValue = "0") int from,
                                       @RequestParam(required = false, defaultValue = "10") int size) {
        EventSearchParam eventSearchParam = new EventSearchParam(LocalDateTime.MIN, from, size, List.of(userId), false);
        return eventMapper.toShortDto(eventService.findAll(eventSearchParam));
    }

    @GetMapping("{eventId}")
    public EventFullDto findOne(@PathVariable Long userId,
                                @PathVariable Long eventId) {
        return eventService.findEventDtoByUserAndId(userId, eventId);
    }

    @GetMapping("{eventId}/requests")
    public List<ParticipationRequestDto> findAllEventRequests(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        return requestMapper.toDto(requestService.findAllEventRequests(userId, eventId));
    }

    @PatchMapping("{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequest(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @Valid @RequestBody EventRequestStatusUpdateRequest requestDto) {
        return requestService.updateStatusRequest(userId, eventId, requestDto);
    }

}
