package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film addLikeToFilm(Long userId, Long filmId);

    Film removeLikeFromFilm(Long userId, Long filmId);

    List<Film> getPopularFilms(int count);
}
