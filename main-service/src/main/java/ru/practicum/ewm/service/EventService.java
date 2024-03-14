package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.controller.dto.*;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.EventSearchRepository;
import ru.practicum.stats.client.StatClient;
import ru.practicum.stats.dto.StatResponse;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.util.ComparisonHelper.getValue;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventSearchRepository eventSearchRepository;
    private final EventMapper eventMapper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final StatClient statClient;

    public Event create(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventTimeException("дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }
        User user = userService.findById(userId);
        Event event = eventMapper.toEvent(newEventDto);
        event.setUser(user);
        Category category = categoryService.findById(newEventDto.getCategory());
        event.setCategory(category);
        sendToReview(event);
        eventRepository.save(event);
        return event;
    }

    public Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(
                        () -> new DataNotFoundException(String.format("Эвент с id %d не найден", eventId)));
    }

    public Event findEventById(Long eventId, Long userId) {
        Event event = findEventById(eventId);
        checkEventInitiator(event.getUser().getId(), userId);
        return event;
    }

    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateRequest) {
        if (updateRequest.getStateAction() != null
                && updateRequest.getStateAction() != EventAction.PUBLISH_EVENT
                && updateRequest.getStateAction() != EventAction.REJECT_EVENT) {
            throw new InvalidEventActionException("некорректное действие");
        }
        update(eventId, null, eventMapper.toRequest(updateRequest));
        return findEventDtoById(eventId, false);
    }

    public EventFullDto update(Long eventId, Long userId, UpdateEventUserRequest updateRequest) {
        if (updateRequest.getStateAction() != null
                && updateRequest.getStateAction() != EventAction.SEND_TO_REVIEW
                && updateRequest.getStateAction() != EventAction.CANCEL_REVIEW) {;
            throw new InvalidEventActionException("некорректное действие");
        }
        Event event = findEventById(eventId);
        if (event.getStatus() == EventStatus.PUBLISHED) {
            throw new InvalidEventActionException("некорректное действие");
        }
        update(eventId, userId, eventMapper.toRequest(updateRequest));
        return findEventDtoById(eventId, false);
    }

    public List<Event> findAll(List<Long> eventIds) {
        return eventRepository.findAllById(eventIds);
    }

    public List<EventFullDto> findAll(EventSearchParam eventSearchParam) {
        if (eventSearchParam.getRangeStart() != null
                && eventSearchParam.getRangeEnd() != null
                && eventSearchParam.getRangeStart().isAfter(eventSearchParam.getRangeEnd())) {
            throw new ValidationException("Время начало позже времини окончания");
        }

        System.out.println("calling eventSearchRepository.findAll");
        List<EventFullDto> events = eventSearchRepository.findAll(eventSearchParam);
        System.out.println("eventSearchRepository.findAll done");

        events.forEach(System.out::println);

        List<String> uris = events.stream()
                .map(dto -> "/events/" + dto.getId())
                .collect(Collectors.toList());

        LocalDateTime minDate = events.stream()
                .map(EventFullDto::getCreatedOn)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());
        LocalDateTime maxDate = LocalDateTime.now();
        List<StatResponse> statistics = statClient.getStatistic(minDate, maxDate, uris, true);

        events.forEach(event -> event.setViews(getHitsForEvent(event, statistics)));

        if ("VIEWS".equals(eventSearchParam.getSort())) {
            events.sort(Comparator.comparing(EventFullDto::getViews).reversed());
        }
        return events;
    }

    public EventFullDto findEventDtoById(Long eventId, boolean showPublishedOnly) {
        EventFullDto event = eventSearchRepository.findById(eventId, showPublishedOnly)
                .orElseThrow(() -> new DataNotFoundException(String.format("Эвент с id %d не найден", eventId)));
        List<String> uris = List.of("/events/" + eventId);
        LocalDateTime minDate = event.getCreatedOn();
        LocalDateTime maxDate = LocalDateTime.now().plusDays(1);
        List<StatResponse> statistics = statClient.getStatistic(minDate, maxDate, uris, true);
        event.setViews(getHitsForEvent(event, statistics));
        return event;
    }

    public EventFullDto findEventDtoByUserAndId(Long userId, Long eventId) {
        userService.findById(userId);
        EventFullDto event = findEventDtoById(eventId, false);
        checkEventInitiator(event.getInitiator().getId(), userId);
        return event;
    }


    public void checkEventInitiator(Long eventInitiatorId, Long userId) {
        if (!eventInitiatorId.equals(userId)) {
            throw new DataNotFoundException("Эвент не найден");
        }
    }

    private long getHitsForEvent(EventFullDto event, List<StatResponse> statistics) {
        return statistics.stream()
                .filter(stat -> stat.getUri().equals("/events/" + event.getId()))
                .map(StatResponse::getHits)
                .findFirst()
                .orElse(0L);
    }

    private Event update(Long eventId, Long userId, UpdateEventRequest updateRequest) {
        if (userId != null) {
            userService.findById(userId);
        }
        Event event = findEventById(eventId);
        if (userId != null && !userId.equals(event.getUser().getId())) {
            throw new ValidationException("можно редактировать только свои события");
        }
        updateEvent(event, updateRequest);
        if (updateRequest.getStateAction() != null) {
            changeEventStatus(event, updateRequest.getStateAction());
        }
        eventRepository.save(event);
        return event;
    }

    private void updateEvent(Event event, UpdateEventRequest updateRequest) {
        event.setAnnotation(getValue(updateRequest.getAnnotation(), event.getAnnotation()));
        event.setDescription(getValue(updateRequest.getDescription(), event.getDescription()));
        event.setEventDate(getValue(updateRequest.getEventDate(), event.getEventDate()));
        event.setPaid(getValue(updateRequest.getPaid(), event.isPaid()));
        event.setParticipantLimit(getValue(updateRequest.getParticipantLimit(), event.getParticipantLimit()));
        event.setRequestModeration(getValue(updateRequest.getRequestModeration(), event.isRequestModeration()));
        event.setTitle(getValue(updateRequest.getTitle(), event.getTitle()));
        if (updateRequest.getCategory() != null) {
            Category category = categoryService.findById(updateRequest.getCategory());
            event.setCategory(category);
        }
        if (updateRequest.getLocation() != null) {
            event.setLat(updateRequest.getLocation().getLat());
            event.setLon(updateRequest.getLocation().getLon());
        }
    }

    private void changeEventStatus(Event event, EventAction action) {
        switch (action) {
            case PUBLISH_EVENT:
                publishEvent(event);
                break;
            case REJECT_EVENT:
                rejectEvent(event);
                break;
            case CANCEL_REVIEW:
                cancelEvent(event);
                break;
            case SEND_TO_REVIEW:
                sendToReview(event);
                break;
            default:
                break;
        }
    }

    private void sendToReview(Event event) {
        if (event.getStatus() != EventStatus.NEW && event.getStatus() != EventStatus.CANCELED) {
            throw new EventTimeException("событие нельзя отправить на модерацию");
        }
        event.setStatus(EventStatus.PENDING);
    }

    private void publishEvent(Event event) {
        if (event.getEventDate().isBefore(LocalDateTime.now().minusHours(1))) {
            throw new EventTimeException("дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
        }
        if (!event.getStatus().equals(EventStatus.PENDING)) {
            throw new EventTimeException("событие можно публиковать, только если оно в состоянии ожидания публикации");
        }
        event.setStatus(EventStatus.PUBLISHED);
    }

    private void rejectEvent(Event event) {
        if (!event.getStatus().equals(EventStatus.PENDING)) {
            throw new EventTimeException("событие можно отклонить, только если оно еще не опубликовано");
        }
        event.setStatus(EventStatus.CANCELED);
    }

    private void cancelEvent(Event event) {
        if (!event.getStatus().equals(EventStatus.PENDING)) {
            throw new EventTimeException("событие можно отклонить, только если оно еще не опубликовано");
        }
        event.setStatus(EventStatus.CANCELED);
    }
}
