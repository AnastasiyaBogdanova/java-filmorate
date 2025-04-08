package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAllUsers() {

        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {

        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> removeFriend(@PathVariable("id") Long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> getCommonFriends(
            @PathVariable(required = false) @NotNull(message = "Не указан id пользователя") Long userId,
            @PathVariable(required = false) @NotNull(message = "Не указан id друга") Long friendId
    ) {
        return userService.getCommonFriends(userId, friendId);
    }
}
