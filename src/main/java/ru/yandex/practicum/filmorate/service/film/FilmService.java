package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Component
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final MpaDbStorage mpaStorage;
    private final GenreDbStorage genreStorage;

    public FilmService(FilmDbStorage filmStorage, @Qualifier("userDbStorage") UserDbStorage userStorage, MpaDbStorage mpaStorage, GenreDbStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public FilmDto createFilm(Film film) {
        findMpaById(film.getMpa().getId());
        if (film.getGenres() != null) {
            findGenreByIds(film.getGenres().stream()
                    .map(Genre::getId)
                    .collect(Collectors.toSet()));
        }
        return FilmMapper.mapToFilmDto(filmStorage.createFilm(film));
    }


    public FilmDto updateFilm(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        findFilmById(film.getId());
        findMpaById(film.getMpa().getId());
        if (film.getGenres() != null) {
            findGenreByIds(film.getGenres().stream()
                    .map(Genre::getId)
                    .collect(Collectors.toSet()));
        }
        return FilmMapper.mapToFilmDto(filmStorage.updateFilm(film));
    }

    public Optional<FilmDto> getFilmById(Long filmId) {
        return Optional.of(FilmMapper.mapToFilmDto(filmStorage.getFilmById(filmId).get()));
    }

    public List<FilmDto> getAllFilms() {
        return filmStorage.getAllFilms()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto addLikeToFilm(Long filmId, Long userId) {
        findFilmById(filmId);
        findUserById(userId);
        return FilmMapper.mapToFilmDto(filmStorage.addLikeToFilm(filmId, userId));
    }

    public FilmDto removeLikeFromFilm(Long filmId, Long userId) {
        findFilmById(filmId);
        findUserById(userId);
        return FilmMapper.mapToFilmDto(filmStorage.removeLikeFromFilm(filmId, userId));
    }

    public List<FilmDto> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count)
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    private User findUserById(Long id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    private Film findFilmById(Long id) {
        return filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден"));
    }

    private Mpa findMpaById(Long id) {
        return mpaStorage.getMpaById(id).orElseThrow(() -> new NotFoundException("Рейтинг с id = " + id + " не найден"));
    }

    private List<Genre> findGenreByIds(Set<Long> ids) {
        List<Genre> genres = ids.stream()
                .map(genreStorage::getGenreById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (genres.size() != ids.size()) {
            throw new NotFoundException("жанр не найден");
        }
        return genres;

    }
}
