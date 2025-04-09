package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        findFilmById(film.getId());
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Long filmId) {
        findFilmById(filmId);
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addLikeToFilm(Long filmId, Long userId) {
        findFilmById(filmId);
        findUserById(userId);
        return filmStorage.addLikeToFilm(filmId, userId);
    }

    public Film removeLikeFromFilm(Long filmId, Long userId) {
        findFilmById(filmId);
        findUserById(userId);
        return filmStorage.removeLikeFromFilm(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    private User findUserById(Long id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    private Film findFilmById(Long id) {
        return filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден"));
    }
}
