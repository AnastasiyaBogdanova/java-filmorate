package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;


@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addLikeToFilm(Long filmId, Long userId) {
        userStorage.checkUser(userId);
        return filmStorage.addLikeToFilm(filmId, userId);
    }


    public Film removeLikeFromFilm(Long filmId, Long userId) {
        userStorage.checkUser(userId);
        return filmStorage.removeLikeFromFilm(filmId, userId);
    }


    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
