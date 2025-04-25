package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.baseRepository.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("userDbStorage")
public class UserDbStorage extends BaseRepository implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(name, login, birthday, email)" +
            " VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET name = ?, login = ?, birthday = ?, email = ? WHERE id = ?";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String GET_FRIENDS_QUERY = "SELECT u.* FROM users u JOIN friends f ON u.id = f.friend_id WHERE f.user_id = ?";
    private static final String GET_COMMON_FRIENDS_QUERY = """
                SELECT u.* FROM friends f1
                JOIN friends f2 ON f1.friend_id = f2.friend_id
                JOIN users u ON f1.friend_id = u.id
                WHERE f1.user_id = ? AND f2.user_id = ?
            """;

    public UserDbStorage(JdbcTemplate jdbc, @Qualifier("userMapper") RowMapper mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        long id = insert(
                INSERT_QUERY,
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getEmail()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        User oldUser = getUserById(newUser.getId()).get();
        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }
        if (newUser.getBirthday() != null) {
            oldUser.setBirthday(newUser.getBirthday());
        }
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getLogin() != null) {
            oldUser.setLogin(newUser.getLogin());
        }

        if (oldUser.getName() == null || oldUser.getName().isBlank()) {
            oldUser.setName(oldUser.getLogin());
        }
        update(
                UPDATE_QUERY,
                oldUser.getName(),
                oldUser.getLogin(),
                oldUser.getBirthday(),
                oldUser.getEmail(),
                oldUser.getId()
        );
        return oldUser;
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    @Override
    public List<User> getAllUsers() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        jdbc.update(INSERT_FRIEND_QUERY, userId, friendId);
        return getUserById(userId).orElseThrow();
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        jdbc.update(DELETE_FRIEND_QUERY, userId, friendId);
        return getUserById(userId).orElseThrow();
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        return jdbc.query(GET_COMMON_FRIENDS_QUERY, mapper, userId, friendId);
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        return jdbc.query(GET_FRIENDS_QUERY, mapper, userId);
    }

    @Override
    public Optional<User> findById(Long id) {
        return getUserById(id);
    }
}
