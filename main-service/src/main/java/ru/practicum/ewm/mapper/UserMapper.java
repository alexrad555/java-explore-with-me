package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.controller.dto.NewUserRequest;
import ru.practicum.ewm.controller.dto.UserDto;
import ru.practicum.ewm.controller.dto.UserShortDto;
import ru.practicum.ewm.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    UserShortDto toShortDto(User user);

    User toUser(NewUserRequest newUserRequest);
}
