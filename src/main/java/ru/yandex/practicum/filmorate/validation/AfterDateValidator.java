package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AfterDateValidator implements ConstraintValidator<AfterDate, LocalDate> {
    private LocalDate minDate;

    @Override
    public void initialize(AfterDate constraintAnnotation) {
        this.minDate = LocalDate.parse(
                constraintAnnotation.value(),
                DateTimeFormatter.ISO_LOCAL_DATE
        );
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true;
        }
        return date.isAfter(minDate) || date.equals(minDate);
    }
}