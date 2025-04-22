package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.baseRepository.BaseRepository;
import ru.yandex.practicum.filmorate.storage.genre.GenreMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaMapper;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorage extends BaseRepository implements FilmStorage {
    private final JdbcTemplate jdbc;
    private static final String FIND_ALL_QUERY = "SELECT f.*, m.name as mpa_name FROM films f LEFT JOIN mpas m on f.mpa = m.id";
    private static final String FIND_BY_ID_QUERY =
            "SELECT f.*, r.name AS mpa_name FROM films f JOIN mpas r ON f.mpa = r.id WHERE f.id = ?";

    private static final String INSERT_QUERY = "INSERT INTO films(name, release_date, description, duration, mpa)" +
            " VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_LIKE = "INSERT INTO likes(film_id, user_id)" +
            " VALUES (?, ?)";
    private static final String REMOVE_LIKE = "DELETE from likes WHERE film_id = ? AND user_id = ?";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, release_date = ?, description = ?," +
            " duration = ?, mpa = ? WHERE id = ?";
    private static final String FIND_POPULAR = "SELECT f.*, COUNT(l.user_id) AS likes_count, m.name as mpa_name " +
            "FROM films f LEFT JOIN likes l ON f.id = l.film_id " +
            "LEFT JOIN mpas m ON f.mpa = m.id " +
            "GROUP BY f.id ORDER BY likes_count DESC LIMIT ?";


    public FilmDbStorage(JdbcTemplate jdbc, @Qualifier("filmMapper") RowMapper mapper) {
        super(jdbc, mapper);
        this.jdbc = jdbc;
    }


    @Override
    public Film createFilm(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        if (film.getGenres() != null) {
            saveGenresForFilm(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        System.out.println("newFilm " + newFilm);
        Film currentFilm = getFilmById(newFilm.getId()).get();
        System.out.println("currentFilm " + currentFilm);
        if (newFilm.getName() != null) {
            currentFilm.setName(newFilm.getName());
        }
        if (newFilm.getDescription() != null) {
            currentFilm.setDescription(newFilm.getDescription());
        }
        if (newFilm.getReleaseDate() != null) {
            currentFilm.setReleaseDate(newFilm.getReleaseDate());
        }
        if (newFilm.getDuration() != null) {
            currentFilm.setDuration(newFilm.getDuration());
        }
        if (newFilm.getMpa() != null) {
            currentFilm.setMpa(newFilm.getMpa());
        }
        update(
                UPDATE_QUERY,
                currentFilm.getName(),
                currentFilm.getReleaseDate(),
                currentFilm.getDescription(),
                currentFilm.getDuration(),
                currentFilm.getMpa().getId(),
                currentFilm.getId()
        );
        if (newFilm.getGenres() != null) {
            updateGenresForFilm(currentFilm.getId(),
                    newFilm.getGenres().stream()
                            .map(Genre::getId)
                            .collect(Collectors.toSet())
            );
            currentFilm.setGenres(newFilm.getGenres());
        }
        return currentFilm;

    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        Optional<Film> film = findOne(FIND_BY_ID_QUERY, filmId);
        film.ifPresent(f -> f.setUserIdLikes(loadLikesForFilm(f.getId())));
        film.ifPresent(f -> f.setGenres(loadGenresForFilm(f.getId())));
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = findMany(FIND_ALL_QUERY);
        films.forEach(film -> {
            film.setUserIdLikes(loadLikesForFilm(film.getId()));
            film.setGenres(loadGenresForFilm(film.getId()));
            film.setMpa(loadMpaForFilm(film.getId()));
        });
        return films;
    }

    @Override
    public Film addLikeToFilm(Long filmId, Long userId) {
        long id = insert(
                INSERT_LIKE,
                filmId,
                userId
        );
        return getFilmById(filmId).get();
    }

    @Override
    public Film removeLikeFromFilm(Long filmId, Long userId) {
        delete(REMOVE_LIKE,
                filmId,
                userId
        );
        return getFilmById(filmId).get();
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> films = findMany(FIND_POPULAR, count);
        films.forEach(film -> {
            film.setUserIdLikes(loadLikesForFilm(film.getId()));
            film.setGenres(loadGenresForFilm(film.getId()));
        });
        return films;
    }

    @Override
    public Optional<Film> findById(Long id) {
        return getFilmById(id);
    }

    private Set<Long> loadLikesForFilm(Long filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        List<Long> likes = jdbc.queryForList(sql, Long.class, filmId);
        return new HashSet<>(likes);
    }

    private Collection<Genre> loadGenresForFilm(Long filmId) {
        String sql = "SELECT distinct g.id, g.name FROM genres g JOIN film_genres fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
        return jdbc.query(sql, new GenreMapper(), filmId);
    }

    private Mpa loadMpaForFilm(Long filmId) {
        String sql = "SELECT m.id, m.name FROM mpas m JOIN films f ON m.id = f.mpa WHERE f.id = ?";
        return jdbc.query(sql, new MpaMapper(), filmId).getFirst();
    }

    private void saveGenresForFilm(Long filmId, Collection<Genre> genres) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        jdbc.batchUpdate(sql, genres.stream()
                .map(genre -> new Object[]{filmId, genre.getId()})
                .collect(Collectors.toList()));
    }

    private void updateGenresForFilm(Long filmId, Set<Long> newGenreIds) {
        jdbc.update("DELETE FROM film_genres WHERE film_id = ?", filmId);
        saveGenresForFilm(filmId, loadGenresForFilm(filmId));
    }
}
