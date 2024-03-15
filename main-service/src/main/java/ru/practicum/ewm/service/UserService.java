package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.OffsetPageable;
import ru.practicum.ewm.controller.dto.NewUserRequest;
import ru.practicum.ewm.exception.DataNotFoundException;
import ru.practicum.ewm.exception.DuplicateException;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Page<User> findAll(List<Long> ids, int from, int size) {
        Pageable pageable = new OffsetPageable(from, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<User> users = ids == null || ids.isEmpty()
                ? userRepository.findAll(pageable) : userRepository.findByIdList(ids, pageable);
        return users;
    }

    public User create(NewUserRequest newUserRequest) {
        User user = userMapper.toUser(newUserRequest);
        if (userRepository.findByEmail(newUserRequest.getEmail()).isPresent()) {
            throw new DuplicateException("email занят");
        }
        userRepository.save(user);
        return user;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new DataNotFoundException(String.format("Пользователь с id %d не найден", userId)));
    }

    public void delete(Long userId) {
        findById(userId);
        userRepository.softDelete(userId);
    }
}
