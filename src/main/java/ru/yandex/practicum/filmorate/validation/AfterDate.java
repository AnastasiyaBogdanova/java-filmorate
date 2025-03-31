package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AfterDateValidator.class) // Указываем класс-валидатор
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterDate {
    String message() default "Дата релиза — не раньше {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();
}