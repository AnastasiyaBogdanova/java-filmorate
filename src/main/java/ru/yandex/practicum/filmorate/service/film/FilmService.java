package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;


@Service
@Component
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addLikeToFilm(Long filmId, Long userId) {
        return filmStorage.addLikeToFilm(filmId, userId);
    }


    public Film removeLikeFromFilm(Long filmId, Long userId) {
        return filmStorage.removeLikeFromFilm(filmId, userId);
    }


    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
