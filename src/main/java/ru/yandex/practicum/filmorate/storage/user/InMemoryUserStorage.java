package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> userHashMap = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userHashMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (!userHashMap.containsKey(newUser.getId())) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        User oldUser = userHashMap.get(newUser.getId());
        log.info("oldUser: " + oldUser.toString());
        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }
        if (newUser.getBirthday() != null) {
            oldUser.setBirthday(newUser.getBirthday());
        }
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }
        if (newUser.getLogin() != null) {
            oldUser.setLogin(newUser.getLogin());
        }
        log.info("newUser: " + oldUser);
        return oldUser;
    }

    @Override
    public List<User> getAllUsers() {
        return userHashMap.values().stream().toList();
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        if (!userHashMap.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (!userHashMap.containsKey(friendId)) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        User user = userHashMap.get(userId);
        user.getFriends().add(friendId);
        log.info("user1: " + user);
        userHashMap.put(userId, user);

        user = userHashMap.get(friendId);
        user.getFriends().add(userId);
        log.info("user2: " + user);
        userHashMap.put(friendId, user);
        return user;
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        if (!userHashMap.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (!userHashMap.containsKey(friendId)) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        User user = userHashMap.get(userId);
        if (!user.getFriends().contains(friendId)) {
            throw new NotFoundException("Пользователя с Id: " + friendId + " нет в друзьях");
        }
        user.getFriends().remove(friendId);
        log.info("user1: " + user);
        userHashMap.put(userId, user);

        user = userHashMap.get(friendId);
        if (!user.getFriends().contains(userId)) {
            throw new NotFoundException("Пользователя с Id: " + userId + " нет в друзьях");
        }
        user.getFriends().remove(userId);
        log.info("user2: " + user);
        userHashMap.put(friendId, user);

        return user;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        if (!userHashMap.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (!userHashMap.containsKey(friendId)) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        Set<Long> user = userHashMap.get(userId).getFriends();
        log.info("userId: " + userId + " " + user);
        Set<Long> friend = userHashMap.get(friendId).getFriends();
        log.info("friendId: " + friendId + " " + friend);
        List<User> users = user.stream()
                .filter(friend::contains)
                .map(userHashMap::get)
                .toList();
        return users;
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        if (!userHashMap.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        return userHashMap.get(userId).getFriends().stream().map(userHashMap::get).toList();
    }

    private long getNextId() {
        long currentMaxId = userHashMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
