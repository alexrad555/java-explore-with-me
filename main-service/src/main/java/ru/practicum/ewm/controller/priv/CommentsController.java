package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controller.dto.CommentShortDto;
import ru.practicum.ewm.controller.dto.NewCommentDto;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestControllerAdvice
@RestController
public class CommentsController {

    @PostMapping("events/{eventId}/comments")
    public CommentShortDto create(@PathVariable Long eventId,
                                  @RequestParam Long userId,
                                  @Valid @RequestBody NewCommentDto commentDto) {

    }

    @PatchMapping("events/{eventId}/comments/{commentId}")
    public CommentShortDto update(@PathVariable Long eventId,
                                  @RequestParam Long userId,
                                  @Valid @RequestBody NewCommentDto commentDto) {

    }
}
