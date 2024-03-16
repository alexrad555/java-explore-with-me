package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controller.dto.CommentShortDto;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.service.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@RestController
@RequestMapping("events/{eventId}/comments")
public class PublicCommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public List<CommentShortDto> findAllPublishedByEventId(@PathVariable Long eventId,
                                                           @RequestParam(required = false) String sort,
                                                           @RequestParam(required = false, defaultValue = "0") int from,
                                                           @RequestParam(required = false, defaultValue = "10") int size) {
        return commentMapper.toShortDto(commentService.findAllPublishedByEventId(eventId, sort, from, size));
    }
}
