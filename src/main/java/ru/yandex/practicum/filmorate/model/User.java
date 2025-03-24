package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    Long id;

    @NotNull(message = "Email не может быть пустым")
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    String email;

    @NotNull(message = "Логин не может быть пустым")
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелы")
    String login;

    String name;

    @Past(message = "Дата рождения не может быть в будущем")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = (name == null || name.isBlank()) ? login : name;
        this.birthday = birthday;
    }
}
