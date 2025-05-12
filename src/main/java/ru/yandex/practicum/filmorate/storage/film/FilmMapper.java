package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("filmMapper")
public class FilmMapper implements RowMapper<Film> {
    private final JdbcTemplate jdbcTemplate;

    public FilmMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("mpa"));
        mpa.setName(rs.getString("mpa_name"));
        film.setMpa(mpa);

        film.setUserIdLikes(loadLikes(film.getId()));
        film.setGenres(loadGenresForFilm(film.getId()));
        return film;
    }

    private Set<Long> loadLikes(Long filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        List<Long> likes = jdbcTemplate.queryForList(sql, Long.class, filmId);
        return new HashSet<>(likes);
    }

    private List<Long> loadGenres(Long filmId) {
        String sql = "SELECT genre_id, name FROM film_genres WHERE film_id = ?";
        List<Long> genres = jdbcTemplate.queryForList(sql, Long.class, filmId);
        return genres;
    }

    private Collection<Genre> loadGenresForFilm(Long filmId) {
        String sql = "SELECT g.id, g.name FROM genres g JOIN film_genres fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, new GenreMapper(), filmId);
    }
}