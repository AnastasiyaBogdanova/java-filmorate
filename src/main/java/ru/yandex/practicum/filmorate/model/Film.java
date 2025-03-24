package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.validation.AfterDate;

import java.time.LocalDate;

@Data
public class Film {
    Long id;

    @NotNull(message = "Название не может быть пустым")
    @NotBlank(message = "Название не может быть пустым")
    String name;

    @Length(max = 200, message = "Максимальная длина описания — 200 символов")
    String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @AfterDate(value = "1895-12-28")
    LocalDate releaseDate;

    @Positive
    Integer duration;
}
