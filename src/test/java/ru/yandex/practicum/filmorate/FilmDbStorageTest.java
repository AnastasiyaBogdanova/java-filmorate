package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class})
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;
    private User testUser;
    private User testUser2;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("Test User");
        testUser.setLogin("testLogin");
        testUser.setEmail("test@mail.ru");
        testUser.setBirthday(LocalDate.of(1990, 1, 1));

        testUser2 = new User();
        testUser2.setName("Test User2");
        testUser2.setLogin("testLogin2");
        testUser2.setEmail("test2@mail.ru");
        testUser2.setBirthday(LocalDate.of(1992, 2, 2));
    }

    @Test
    void testCreateAndRetrieveFilm() {
        Film film = new Film();
        film.setName("Новый фильм");
        film.setDescription("Описание нового фильма");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDuration(120);

        Mpa mpa = new Mpa();
        mpa.setId(1L);
        film.setMpa(mpa);

        List<Genre> genres = new ArrayList<>();
        Genre genre = new Genre();
        genre.setId(1L);
        genres.add(genre);
        film.setGenres(genres);

        Film createdFilm = filmStorage.createFilm(film);

        Optional<Film> retrievedFilmOpt = filmStorage.getFilmById(createdFilm.getId());
        assertThat(retrievedFilmOpt).isPresent();
        Film retrievedFilm = retrievedFilmOpt.get();

        assertThat(retrievedFilm.getName()).isEqualTo("Новый фильм");
        assertThat(retrievedFilm.getDescription()).isEqualTo("Описание нового фильма");
        assertThat(retrievedFilm.getReleaseDate()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(retrievedFilm.getDuration()).isEqualTo(120);
        assertThat(retrievedFilm.getMpa().getId()).isEqualTo(1L);
        assertThat(retrievedFilm.getGenres().size()).isEqualTo(1);
        assertThat(retrievedFilm.getGenres().iterator().next().getId()).isEqualTo(1L);
    }

    @Test
    void testUpdateFilm() {
        Film initialFilm = new Film();
        initialFilm.setName("Первый фильм");
        initialFilm.setDescription("Исходное описание");
        initialFilm.setReleaseDate(LocalDate.of(2022, 1, 1));
        initialFilm.setDuration(100);

        Mpa mpa = new Mpa();
        mpa.setId(1L);
        initialFilm.setMpa(mpa);

        Film savedFilm = filmStorage.createFilm(initialFilm);

        Film updatedFilm = new Film();
        updatedFilm.setId(savedFilm.getId());
        updatedFilm.setName("Обновленный фильм");
        updatedFilm.setDescription("Новое описание");
        updatedFilm.setReleaseDate(LocalDate.of(2023, 1, 1));
        updatedFilm.setDuration(120);

        Mpa newMpa = new Mpa();
        newMpa.setId(2L);
        updatedFilm.setMpa(newMpa);

        filmStorage.updateFilm(updatedFilm);

        Optional<Film> result = filmStorage.getFilmById(savedFilm.getId());

        assertThat(result).isPresent();
        Film actualUpdatedFilm = result.get();

        assertThat(actualUpdatedFilm.getName()).isEqualTo("Обновленный фильм");
        assertThat(actualUpdatedFilm.getDescription()).isEqualTo("Новое описание");
        assertThat(actualUpdatedFilm.getReleaseDate()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(actualUpdatedFilm.getDuration()).isEqualTo(120);
        assertThat(actualUpdatedFilm.getMpa().getId()).isEqualTo(2L);
    }

    @Test
    void testAddLike() {

        Film film = new Film();
        film.setName("Фильм для лайков");
        film.setDescription("Тестовый фильм");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(90);

        Mpa mpa = new Mpa();
        mpa.setId(1L);
        film.setMpa(mpa);

        Film createdFilm = filmStorage.createFilm(film);

        filmStorage.addLikeToFilm(createdFilm.getId(), testUser.getId());

        Optional<Film> likedFilmOpt = filmStorage.getFilmById(createdFilm.getId());
        assertThat(likedFilmOpt).isPresent();
        Film likedFilm = likedFilmOpt.get();
        assertThat(likedFilm.getUserIdLikes()).contains(testUser.getId());
    }

    @Test
    void testGetPopularFilms() {
        Mpa mpa = new Mpa();
        mpa.setId(1L);

        Film film = new Film();
        film.setName("Фильм для лайков");
        film.setDescription("Тестовый фильм");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(90);
        film.setMpa(mpa);


        Film film2 = new Film();
        film2.setName("Фильм для лайков");
        film2.setDescription("Тестовый фильм");
        film2.setReleaseDate(LocalDate.now());
        film2.setDuration(90);
        film2.setMpa(mpa);


        Film film3 = new Film();
        film3.setName("Фильм для лайков");
        film3.setDescription("Тестовый фильм");
        film3.setReleaseDate(LocalDate.now());
        film3.setDuration(90);
        film3.setMpa(mpa);


        filmStorage.createFilm(film);
        filmStorage.createFilm(film2);
        filmStorage.createFilm(film3);

        filmStorage.addLikeToFilm(film.getId(), testUser.getId());
        filmStorage.addLikeToFilm(film.getId(), testUser2.getId());
        filmStorage.addLikeToFilm(film2.getId(), testUser.getId());

        List<Film> popularFilms = filmStorage.getPopularFilms(3);

        assertThat(popularFilms.size()).isEqualTo(3);

        assertThat(popularFilms.get(0).getId()).isEqualTo(film.getId());

    }

    @Test
    void testGetAllFilms() {
        filmStorage.getAllFilms().clear();

        int numberOfFilms = 5;
        for (int i = 0; i < numberOfFilms; i++) {
            Film film = new Film();
            film.setName("Фильм №" + i);
            film.setDescription("Описание фильма №" + i);
            film.setReleaseDate(LocalDate.now());
            film.setDuration(i * 10 + 100);

            Mpa mpa = new Mpa();
            mpa.setId((long) (i % 3 + 1));
            film.setMpa(mpa);

            filmStorage.createFilm(film);
        }

        List<Film> allFilms = filmStorage.getAllFilms();
        assertThat(allFilms.size()).isEqualTo(numberOfFilms);
    }

    @Test
    void testGetNonExistingFilm() {
        Optional<Film> nonExistentFilm = filmStorage.getFilmById(-1L);
        assertThat(nonExistentFilm).isEmpty();
    }
}