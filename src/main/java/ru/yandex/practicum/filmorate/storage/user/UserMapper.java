package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("userMapper")
public class UserMapper implements RowMapper<User> {
    private final JdbcTemplate jdbcTemplate;

    public UserMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setLogin(resultSet.getString("login"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        user.setEmail(resultSet.getString("email"));
        user.setFriends(loadFriends(user.getId()));
        return user;
    }

    private Set<Long> loadFriends(Long userId) {
        String sql = "SELECT friend_id FROM friends WHERE user_id = ?";
        List<Long> friends = jdbcTemplate.queryForList(sql, Long.class, userId);
        return new HashSet<>(friends);
    }
}