package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreDbStorage.class})
class GenreDbStorageTest {

    private final GenreDbStorage genreStorage;

    @Test
    void testGetGenreById() {
        Optional<Genre> genreOptional = genreStorage.getGenreById(1L);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre -> {
                    assertThat(genre.getId()).isEqualTo(1L);
                    assertThat(genre.getName()).isEqualTo("Комедия");
                });
    }

    @Test
    void testGetAllGenres() {
        List<Genre> genres = genreStorage.getAllGenre();

        assertThat(genres)
                .hasSize(6)
                .extracting(Genre::getName)
                .containsExactlyInAnyOrder(
                        "Комедия",
                        "Драма",
                        "Мультфильм",
                        "Триллер",
                        "Документальный",
                        "Боевик"
                );
    }

    @Test
    void testGetNonExistingGenre() {
        Optional<Genre> genreOptional = genreStorage.getGenreById(999L);
        assertThat(genreOptional).isEmpty();
    }
}