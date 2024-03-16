package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controller.dto.CommentFullDto;
import ru.practicum.ewm.controller.dto.CommentShortDto;
import ru.practicum.ewm.controller.dto.NewCommentDto;
import ru.practicum.ewm.controller.dto.UpdateUserCommentDto;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@RestController
@RequestMapping("users/{userId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentShortDto create(@PathVariable Long userId,
                                  @RequestParam Long eventId,
                                  @Valid @RequestBody NewCommentDto commentDto) {
        return commentMapper.toShortDto(commentService.create(eventId, userId, commentDto));
    }

    @PatchMapping("{commentId}")
    public CommentShortDto update(@PathVariable Long userId,
                                  @PathVariable Long commentId,
                                  @Valid @RequestBody UpdateUserCommentDto commentDto) {
        return commentMapper.toShortDto(commentService.update(userId, commentId, commentDto));
    }

    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId,
                       @PathVariable Long commentId) {
        commentService.delete(userId, commentId);
    }

    @GetMapping
    public List<CommentFullDto> findAll(@PathVariable Long userId,
                                        @RequestParam(required = false) String sort,
                                        @RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        return commentMapper.toFullDto(commentService.findAllByAuthorId(userId, sort, from, size));
    }
}
