package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmHashMap = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(getNextId());
        filmHashMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        return Optional.of(filmHashMap.get(filmId));
    }

    @Override
    public Film updateFilm(Film newFilm) {
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
        Film film = filmHashMap.get(filmId);
        film.getUserIdLikes().add(userId);
        log.info("film: " + film);
        filmHashMap.put(filmId, film);
        return film;
    }

    @Override
    public Film removeLikeFromFilm(Long filmId, Long userId) {
        Film film = filmHashMap.get(filmId);
        film.getUserIdLikes().remove(userId);
        log.info("film: " + film);
        filmHashMap.put(filmId, film);
        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmHashMap.values().stream()
                .sorted(Comparator.comparingLong((Film film) -> film.getUserIdLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Optional<Film> findById(Long id) {
        if (filmHashMap.containsKey(id)) {
            return Optional.of(filmHashMap.get(id));
        }
        return Optional.empty();
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
