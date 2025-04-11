package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long userId);

    List<User> getAllUsers();

    User addFriend(Long userId, Long friendId);

    User removeFriend(Long userId, Long friendId);

    List<User> getCommonFriends(Long userId, Long friendId);

    List<User> getAllFriends(Long userId);

    Optional<User> findById(Long id);
}
