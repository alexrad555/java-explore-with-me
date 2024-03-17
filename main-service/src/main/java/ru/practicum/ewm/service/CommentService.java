package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.OffsetPageable;
import ru.practicum.ewm.controller.dto.NewCommentDto;
import ru.practicum.ewm.controller.dto.UpdateUserCommentDto;
import ru.practicum.ewm.exception.DataNotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;

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

    public Comment update(Long userId, Long commentId, UpdateUserCommentDto commentDto) {
        userService.findById(userId);
        Comment comment = findByIdAndUserId(commentId, userId);
        comment.setMessage(commentDto.getMessage());
        comment.setEditDate(LocalDateTime.now());
        comment.setPublishDate(null);
        commentRepository.save(comment);
        return comment;
    }

    public void delete(Long userId, Long commentId) {
        findByIdAndUserId(commentId, userId);
        commentRepository.softDelete(commentId);
    }

    public List<Comment> findAllByAuthorId(Long authorId, String sort, int from, int size) {
        userService.findById(authorId);
        Sort.Direction direction = Sort.Direction.fromOptionalString(sort).orElse(Sort.Direction.DESC);
        Pageable pageable = new OffsetPageable(from, size, Sort.by(direction, "createDate"));
        return commentRepository.findAllByAuthorId(authorId, pageable).toList();
    }

    public List<Comment> findAllNotPublished(Long eventId, String sort, int from, int size) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sort).orElse(Sort.Direction.DESC);
        Pageable pageable = new OffsetPageable(from, size, Sort.by(direction, "createDate"));
        if (eventId == null) {
            return commentRepository.findAllByPublishDateIsNullAndDeletionDateIsNull(pageable).toList();
        }
        eventService.findEventById(eventId);
        return commentRepository.findAllByPublishDateIsNullAndDeletionDateIsNullAndEventId(eventId, pageable).toList();
    }

    public Comment publish(Long commentId) {
        Comment comment = findById(commentId);
        comment.setPublishDate(LocalDateTime.now());
        commentRepository.save(comment);
        return comment;
    }

    public void deleteByAdmin(Long commentId) {
        findById(commentId);
        commentRepository.softDelete(commentId);
    }

    public List<Comment> findAllPublishedByEventId(Long eventId, String sort, int from, int size) {
        eventService.findEventById(eventId);
        Sort.Direction direction = Sort.Direction.fromOptionalString(sort).orElse(Sort.Direction.DESC);
        Pageable pageable = new OffsetPageable(from, size, Sort.by(direction, "createDate"));
        return commentRepository.findAllByPublishDateIsNotNullAndDeletionDateIsNullAndEventId(eventId, pageable).toList();
    }

    private Comment findByIdAndUserId(Long commentId, Long userId) {
        return commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(
                        () -> new DataNotFoundException(String.format("Коментарий с id %d не найден или не доступен", commentId)));
    }

    private Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new DataNotFoundException(String.format("Коментарий с id %d не найден", commentId)));
    }

}
