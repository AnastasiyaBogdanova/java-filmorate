package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.baseRepository.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseRepository implements GenreStorage {
    private final JdbcTemplate jdbc;
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";


    public GenreDbStorage(JdbcTemplate jdbc, @Qualifier("genreMapper") RowMapper mapper) {
        super(jdbc, mapper);
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Genre> getGenreById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Genre> getAllGenre() {
        return findMany(FIND_ALL_QUERY);
    }

}
