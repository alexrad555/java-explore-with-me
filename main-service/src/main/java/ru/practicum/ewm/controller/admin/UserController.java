package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controller.dto.NewUserRequest;
import ru.practicum.ewm.controller.dto.UserDto;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@RestController
@RequestMapping("admin/users")
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping
    List<UserDto> findAll(@RequestParam(required = false) List<Long> ids,
                          @RequestParam(defaultValue = "0", required = false) int from,
                          @RequestParam(defaultValue = "10", required = false) int size) {
        List<UserDto> users = userService.findAll(ids, from, size).map(userMapper::toDto).toList();
        return users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto create(@Valid @RequestBody NewUserRequest newUserRequest) {
        return userMapper.toDto(userService.create(newUserRequest));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }

}
