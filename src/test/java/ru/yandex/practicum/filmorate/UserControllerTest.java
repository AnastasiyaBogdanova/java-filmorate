package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
class UserControllerTest {
    private User user;


    @Test
    void createUser() throws Exception {
        user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("userLogin");
        user.setName("User Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
    }

}
