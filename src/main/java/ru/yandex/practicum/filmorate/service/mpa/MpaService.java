package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaStorage;

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public Optional<Mpa> getMpaById(Long mpaId) {
        findMpaById(mpaId);
        return mpaStorage.getMpaById(mpaId);
    }

    private Mpa findMpaById(Long id) {
        return mpaStorage.getMpaById(id).orElseThrow(() -> new NotFoundException("Рейтинг с id = " + id + " не найден"));
    }
}
