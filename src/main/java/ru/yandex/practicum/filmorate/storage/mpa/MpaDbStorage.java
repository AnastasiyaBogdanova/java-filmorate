package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.baseRepository.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage extends BaseRepository implements MpaStorage {
    private final JdbcTemplate jdbc;
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpas";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpas WHERE id = ?";


    public MpaDbStorage(JdbcTemplate jdbc, @Qualifier("mpaMapper") RowMapper mapper) {
        super(jdbc, mapper);
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Mpa> getMpaById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return findMany(FIND_ALL_QUERY);
    }
}
