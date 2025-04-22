package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbStorage.class})
class MpaDbStorageTest {

    private final MpaDbStorage mpaStorage;

    @Test
    void testFindMpaById() {
        Optional<Mpa> mpaOptional = mpaStorage.getMpaById(1L);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    void testGetAllMpa() {
        List<Mpa> allMpa = mpaStorage.getAllMpa();

        assertThat(allMpa)
                .hasSize(5)
                .extracting(Mpa::getName)
                .containsExactlyInAnyOrder(
                        "G",
                        "PG",
                        "PG-13",
                        "R",
                        "NC-17"
                );
    }

    @Test
    void testGetNonExistingMpa() {
        Optional<Mpa> mpaOptional = mpaStorage.getMpaById(999L);
        assertThat(mpaOptional).isEmpty();
    }
}