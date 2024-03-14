package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.controller.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.controller.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.exception.DataNotFoundException;
import ru.practicum.ewm.exception.InvalidEventStateException;
import ru.practicum.ewm.exception.RequestValidationException;
import ru.practicum.ewm.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.repository.ParticipationRequestRepository;

import java.util.List;

import static ru.practicum.ewm.model.ParticipationRequestStatus.*;

@Service
@RequiredArgsConstructor
public class ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final ParticipationRequestMapper requestMapper;
    private final EventService eventService;
    private final UserService userService;

    public ParticipationRequest createRequest(Long userId, Long eventId) {
        Event event = eventService.findEventById(eventId);
        if (event.getUser().getId().equals(userId)) {
            throw new RequestValidationException("инициатор события не может добавить запрос на участие в своём событии");
        }
        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new RequestValidationException("нельзя участвовать в неопубликованном событии");
        }
        if (requestRepository.findByEventIdAndRequesterId(eventId, userId).isPresent()) {
            throw new RequestValidationException("нельзя добавить повторный запрос");
        }
        if (eventIsFull(event)) {
            throw new RequestValidationException("достигнут лимит запросов на участие");
        }
        User user = userService.findById(userId);
        ParticipationRequest newRequest = new ParticipationRequest(event, user);
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            newRequest.setStatus(CONFIRMED);
        }
        requestRepository.save(newRequest);
        return newRequest;
    }

    public ParticipationRequest cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = findById(requestId);
        userService.findById(userId);
        if (!request.getRequester().getId().equals(userId)) {
            throw new DataNotFoundException("Запрос не найден или недоступен");
        }
        request.setStatus(CANCELED);
        requestRepository.save(request);
        return request;
    }

    public ParticipationRequest findById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(
                        () -> new DataNotFoundException(String.format("Запрос с id %d не найден", requestId)));
    }

    public List<ParticipationRequest> findAllByUserId(Long userId) {
        userService.findById(userId);
        return requestRepository.findAllByRequesterId(userId);
    }

    public List<ParticipationRequest> findAllEventRequests(Long userId, Long eventId) {
        userService.findById(userId);
        eventService.findEventById(eventId, userId);
        return requestRepository.findAllByEventId(eventId);
    }

    @Transactional
    public EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest requestDto) {
        userService.findById(userId);
        Event event = eventService.findEventById(eventId, userId);
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            throw new RequestValidationException("подтверждение заявок не требуется");
        }
        if (requestDto.getStatus() == CONFIRMED
                && eventIsFull(event)) {
            throw new RequestValidationException("лимит заявок для события исчерпан");
        }
        List<ParticipationRequest> requests = requestRepository.findAllByIdInAndEventId(requestDto.getRequestIds(), eventId);
        for (ParticipationRequest request : requests) {
            if (request.getStatus() != PENDING) {
                throw new InvalidEventStateException("статус можно изменить только у заявок, находящихся в состоянии ожидания");
            }
            if (eventIsFull(event)) {
                break;
            }
            request.setStatus(requestDto.getStatus());
            requestRepository.save(request);
        }
        if (eventIsFull(event)) {
            List<ParticipationRequest> pendingRequests = requestRepository.findAllByEventIdAndStatus(eventId, PENDING);
            pendingRequests.forEach(r -> r.setStatus(REJECTED));
            requestRepository.saveAll(pendingRequests);
        }
        List<ParticipationRequest> confirmedRequests = requestRepository.findAllByEventIdAndStatus(eventId, CONFIRMED);
        List<ParticipationRequest> rejectedRequests = requestRepository.findAllByEventIdAndStatus(eventId, REJECTED);
        return new EventRequestStatusUpdateResult(requestMapper.toDto(confirmedRequests), requestMapper.toDto(rejectedRequests));
    }

    private boolean eventIsFull(Event event) {
        return event.getParticipantLimit() > 0
                && requestRepository
                .countByEventIdAndStatus(event.getId(), CONFIRMED) >= event.getParticipantLimit();
    }
}
