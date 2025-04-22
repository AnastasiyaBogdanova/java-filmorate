package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

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
    public Optional<User> getUserById(Long userId) {
        return Optional.of(userHashMap.get(userId));
    }

    @Override
    public User updateUser(User newUser) {
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
        User user = userHashMap.get(userId);
        user.getFriends().remove(friendId);
        log.info("user1: " + user);
        userHashMap.put(userId, user);

        user = userHashMap.get(friendId);
        user.getFriends().remove(userId);
        log.info("user2: " + user);
        userHashMap.put(friendId, user);

        return user;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
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
        return userHashMap.get(userId).getFriends().stream().map(userHashMap::get).toList();
    }

    @Override
    public Optional<User> findById(Long id) {
        if (userHashMap.containsKey(id)) {
            return Optional.of(userHashMap.get(id));
        }
        return Optional.empty();
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
