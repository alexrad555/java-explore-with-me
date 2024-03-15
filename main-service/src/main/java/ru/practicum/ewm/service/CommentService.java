package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.controller.dto.NewCommentDto;
import ru.practicum.ewm.exception.DataNotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventService eventService;

    public Comment create(Long eventId, Long authorId, NewCommentDto commentDto) {
        Event event = eventService.findEventById(eventId);
        User author = userService.findById(authorId);
        if (event.getUser().getId().equals(authorId)) {
            throw new ValidationException("нельзя оставлять комментарий на свое событие");
        }
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setMessage(commentDto.getMessage());
        commentRepository.save(comment);
        return comment;
    }

    public Comment create(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new DataNotFoundException(String.format("Коментарий с id %d не найден", commentId)));
    }

    public
}
