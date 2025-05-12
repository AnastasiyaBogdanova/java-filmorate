package ru.yandex.practicum.filmorate.service.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDbStorage userStorage;

    public UserDto createUser(User user) {
        return UserMapper.mapToUserDto(userStorage.createUser(user));
    }

    public UserDto updateUser(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        findUserById(user.getId());
        return UserMapper.mapToUserDto(userStorage.updateUser(user));
    }

    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserById(Long userId) {
        return Optional.of(UserMapper.mapToUserDto(findUserById(userId)));
    }

    public UserDto addFriend(Long userId, Long friendId) {
        findUserById(userId);
        findUserById(friendId);
        return UserMapper.mapToUserDto(userStorage.addFriend(userId, friendId));
    }


    public UserDto removeFriend(Long userId, Long friendId) {
        findUserById(userId);
        findUserById(friendId);
        return UserMapper.mapToUserDto(userStorage.removeFriend(userId, friendId));
    }

    public List<UserDto> getAllFriends(Long userId) {
        findUserById(userId);
        return userStorage.getAllFriends(userId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getCommonFriends(Long userId, Long friendId) {
        findUserById(userId);
        findUserById(friendId);
        return userStorage.getCommonFriends(userId, friendId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    private User findUserById(Long id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }
}
