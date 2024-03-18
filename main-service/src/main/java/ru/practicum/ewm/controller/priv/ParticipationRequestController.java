package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controller.dto.ParticipationRequestDto;
import ru.practicum.ewm.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.service.ParticipationRequestService;

import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@RestController
@RequestMapping("users/{userId}/requests")
public class ParticipationRequestController {
    private final ParticipationRequestMapper requestMapper;
    private final ParticipationRequestService participationRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                          @RequestParam Long eventId) {
        return requestMapper.toDto(participationRequestService.createRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        return requestMapper.toDto(participationRequestService.cancelRequest(userId, requestId));
    }

    @GetMapping
    public List<ParticipationRequestDto> findAllByUserId(@PathVariable Long userId) {
        return requestMapper.toDto(participationRequestService.findAllByUserId(userId));
    }
}
