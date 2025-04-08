package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmHashMap = new HashMap<>();
    private final Map<Long, Film> userHashMap;

    public InMemoryFilmStorage(Map<Long, Film> userHashMap) {
        this.userHashMap = userHashMap;
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(getNextId());
        filmHashMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (!filmHashMap.containsKey(filmId)) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        return filmHashMap.get(filmId);
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        if (!filmHashMap.containsKey(newFilm.getId())) {
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }
        Film oldFilm = filmHashMap.get(newFilm.getId());
        log.info("oldFilm: " + oldFilm.toString());
        if (newFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }
        if (newFilm.getDuration() != null) {
            oldFilm.setDuration(newFilm.getDuration());
        }
        if (newFilm.getDescription() != null) {
            oldFilm.setDescription(newFilm.getDescription());
        }
        if (newFilm.getName() != null) {
            oldFilm.setName(newFilm.getName());
        }
        log.info("newFilm: " + oldFilm);
        return oldFilm;
    }

    @Override
    public List<Film> getAllFilms() {
        return filmHashMap.values().stream().toList();
    }

    @Override
    public Film addLikeToFilm(Long filmId, Long userId) {
        if (!filmHashMap.containsKey(filmId)) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        if (!userHashMap.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Film film = filmHashMap.get(filmId);
        film.getUserIdLikes().add(userId);
        log.info("film: " + film);
        filmHashMap.put(filmId, film);
        return film;
    }

    @Override
    public Film removeLikeFromFilm(Long filmId, Long userId) {
        if (!filmHashMap.containsKey(filmId)) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        if (!userHashMap.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Film film = filmHashMap.get(filmId);
        film.getUserIdLikes().remove(userId);
        log.info("film: " + film);
        filmHashMap.put(filmId, film);
        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmHashMap.values().stream()
                .sorted(Comparator.comparingInt(film -> film.getUserIdLikes().size()))
                .limit(count)
                .toList();
    }

    private long getNextId() {
        long currentMaxId = filmHashMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
