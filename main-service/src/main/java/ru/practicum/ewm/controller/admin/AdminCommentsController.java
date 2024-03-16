package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controller.dto.CommentFullDto;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.service.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@RestController
@RequestMapping("admin/comments")
public class AdminCommentsController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping
    public List<CommentFullDto> findAllNotPublished(@RequestParam(required = false) Long eventId,
                                                    @RequestParam(required = false) String sort,
                                                    @RequestParam(required = false, defaultValue = "0") int from,
                                                    @RequestParam(required = false, defaultValue = "10") int size) {
        return commentMapper.toFullDto(commentService.findAllNotPublished(eventId, sort, from, size));
    }

    @PatchMapping("{commentId}")
    public CommentFullDto publish(@PathVariable Long commentId) {
        return commentMapper.toFullDto(commentService.publish(commentId));
    }

    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId) {
        commentService.deleteByAdmin(commentId);
    }
}
