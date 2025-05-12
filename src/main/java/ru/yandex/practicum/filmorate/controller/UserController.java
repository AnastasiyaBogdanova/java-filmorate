package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getAllUsers() {

        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto createUser(@Valid @RequestBody User user) {

        return userService.createUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@Valid @RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto removeFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> removeFriend(@PathVariable("id") Long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id).get();
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getCommonFriends(
            @PathVariable(required = false) @NotNull(message = "Не указан id пользователя") Long userId,
            @PathVariable(required = false) @NotNull(message = "Не указан id друга") Long friendId
    ) {
        return userService.getCommonFriends(userId, friendId);
    }
}
