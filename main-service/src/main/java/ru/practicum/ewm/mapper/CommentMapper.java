package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.controller.dto.CommentFullDto;
import ru.practicum.ewm.controller.dto.CommentShortDto;
import ru.practicum.ewm.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {

    CommentShortDto toShortDto(Comment comment);

    List<CommentShortDto> toShortDto(List<Comment> comment);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "eventTitle", source = "event.title")
    CommentFullDto toFullDto(Comment comment);

    List<CommentFullDto> toFullDto(List<Comment> comment);
}
