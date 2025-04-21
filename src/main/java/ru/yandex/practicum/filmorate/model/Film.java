package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.validation.AfterDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Length(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @AfterDate(value = "1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private Set<Long> userIdLikes = new HashSet<>();
    private Set<String> genre = new HashSet<>();
    private String rating;
}
