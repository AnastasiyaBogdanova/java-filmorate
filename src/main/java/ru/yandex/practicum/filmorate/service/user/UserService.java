package ru.yandex.practicum.filmorate.service.user;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Component
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        findUserById(user.getId());
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long userId) {
        findUserById(userId);
        return userStorage.getUserById(userId);
    }

    public User addFriend(Long userId, Long friendId) {
        findUserById(userId);
        findUserById(friendId);
        return userStorage.addFriend(userId, friendId);
    }


    public User removeFriend(Long userId, Long friendId) {
        findUserById(userId);
        findUserById(friendId);
        return userStorage.removeFriend(userId, friendId);
    }

    public List<User> getAllFriends(Long userId) {
        findUserById(userId);
        return userStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        findUserById(userId);
        findUserById(friendId);
        return userStorage.getCommonFriends(userId, friendId);
    }

    private User findUserById(Long id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }
}
