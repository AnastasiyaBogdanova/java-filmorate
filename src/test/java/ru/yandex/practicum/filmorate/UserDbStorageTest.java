package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
class UserDbStorageTest {

    private final UserDbStorage userStorage;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("Test User");
        testUser.setLogin("testLogin");
        testUser.setEmail("test@mail.ru");
        testUser.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    void testCreateAndGetUser() {
        User createdUser = userStorage.createUser(testUser);

        Optional<User> userOptional = userStorage.getUserById(createdUser.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user.getId()).isEqualTo(createdUser.getId());
                    assertThat(user.getName()).isEqualTo("Test User");
                    assertThat(user.getLogin()).isEqualTo("testLogin");
                    assertThat(user.getEmail()).isEqualTo("test@mail.ru");
                });
    }

    @Test
    void testUpdateUser() {
        User createdUser = userStorage.createUser(testUser);

        User updatedUser = new User();
        updatedUser.setId(createdUser.getId());
        updatedUser.setName("Updated Name");
        updatedUser.setLogin(createdUser.getLogin());
        updatedUser.setEmail("updated@mail.ru");
        updatedUser.setBirthday(createdUser.getBirthday());

        User result = userStorage.updateUser(updatedUser);

        assertThat(result)
                .hasFieldOrPropertyWithValue("id", createdUser.getId())
                .hasFieldOrPropertyWithValue("name", "Updated Name")
                .hasFieldOrPropertyWithValue("email", "updated@mail.ru");
    }

    @Test
    void testUpdateNonExistingUser() {
        User nonExistingUser = new User();
        nonExistingUser.setId(999L);

        assertThatThrownBy(() -> userStorage.updateUser(nonExistingUser))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testGetAllUsers() {
        userStorage.createUser(testUser);

        User secondUser = new User();
        secondUser.setName("Second User");
        secondUser.setLogin("anotherLogin");
        secondUser.setEmail("another@mail.ru");
        secondUser.setBirthday(LocalDate.of(1995, 5, 5));
        userStorage.createUser(secondUser);

        List<User> users = userStorage.getAllUsers();

        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    void testAddAndGetFriends() {
        User user1 = userStorage.createUser(testUser);

        User user2 = new User();
        user2.setName("Friend User");
        user2.setLogin("friendLogin");
        user2.setEmail("friend@mail.ru");
        user2.setBirthday(LocalDate.of(1995, 5, 5));
        User createdUser2 = userStorage.createUser(user2);

        userStorage.addFriend(user1.getId(), createdUser2.getId());

        List<User> friends = userStorage.getAllFriends(user1.getId());
        assertThat(friends.size()).isEqualTo(1);
        assertThat(friends.get(0).getId()).isEqualTo(createdUser2.getId());
    }

    @Test
    void testRemoveFriend() {
        User user1 = userStorage.createUser(testUser);

        User user2 = new User();
        user2.setName("Friend User");
        user2.setLogin("friendLogin");
        user2.setEmail("friend@mail.ru");
        user2.setBirthday(LocalDate.of(1995, 5, 5));
        User createdUser2 = userStorage.createUser(user2);

        userStorage.addFriend(user1.getId(), createdUser2.getId());
        userStorage.removeFriend(user1.getId(), createdUser2.getId());

        List<User> friends = userStorage.getAllFriends(user1.getId());
        assertThat(friends.size()).isEqualTo(0);
    }

    @Test
    void testGetCommonFriends() {
        User user1 = userStorage.createUser(testUser);

        User user2 = new User();
        user2.setName("Second User");
        user2.setLogin("secondLogin");
        user2.setEmail("second@mail.ru");
        user2.setBirthday(LocalDate.of(1995, 5, 5));
        User createdUser2 = userStorage.createUser(user2);

        User commonFriend = new User();
        commonFriend.setName("Common Friend");
        commonFriend.setLogin("commonFriend");
        commonFriend.setEmail("common@mail.ru");
        commonFriend.setBirthday(LocalDate.of(2000, 1, 1));
        User createdCommonFriend = userStorage.createUser(commonFriend);

        userStorage.addFriend(user1.getId(), createdCommonFriend.getId());
        userStorage.addFriend(createdUser2.getId(), createdCommonFriend.getId());

        List<User> commonFriends = userStorage.getCommonFriends(user1.getId(), createdUser2.getId());
        assertThat(commonFriends.size()).isEqualTo(1);
        assertThat(commonFriends.get(0).getId()).isEqualTo(createdCommonFriend.getId());
    }

    @Test
    void testGetUserWithNonExistingId() {
        Optional<User> userOptional = userStorage.getUserById(999L);
        assertThat(userOptional).isEmpty();
    }

    @Test
    void testCreateUserWithEmptyName() {
        User user = new User();
        user.setLogin("emptyName");
        user.setEmail("empty@mail.ru");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userStorage.createUser(user);

        assertThat(createdUser.getName()).isEqualTo(user.getLogin());
    }
}