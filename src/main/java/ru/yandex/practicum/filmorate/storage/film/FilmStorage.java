package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Long filmId);

    List<Film> getAllFilms();

    Film addLikeToFilm(Long userId, Long filmId);

    Film removeLikeFromFilm(Long userId, Long filmId);

    List<Film> getPopularFilms(int count);
}
