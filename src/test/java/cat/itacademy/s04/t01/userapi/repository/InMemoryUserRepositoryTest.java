package cat.itacademy.s04.t01.userapi.repository;

import cat.itacademy.s04.t01.userapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    @Test
    void saveUser_addsUserToList() {
        User user = new User(UUID.randomUUID(), "Ted Smith", "td@example.com");
        repository.save(user);

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void saveUser_returnsSavedUser() {
        User user = new User(UUID.randomUUID(), "Lana Dana", "ld@example.com");

        User saved = repository.save(user);

        assertEquals(user, saved);
    }

    @Test
    void findAllUsers_returnsEmptyListInitially() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void findAllUsers_returnsAllSavedUsers() {
        User user1 = new User(UUID.randomUUID(), "Jess B", "jb@example.com");
        User user2 = new User(UUID.randomUUID(), "Lu C", "lc@example.com");

        repository.save(user1);
        repository.save(user2);

        List<User> users = repository.findAll();

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void findUserById_returnsUserWhenExists() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Jon D", "jd@example.com");
        repository.save(user);

        Optional<User> found = repository.findById(id);

        assertTrue(found.isPresent());
        assertEquals(user, found.get());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void findUserById_returnsEmptyWhenNotExists() {
        UUID id = UUID.randomUUID();

        assertTrue(repository.findById(id).isEmpty());
    }

    @Test
    void searchUserByName_returnsMatchingUsers() {
        User user1 = new User(UUID.randomUUID(), "Gina P", "gp@example.com");
        User user2 = new User(UUID.randomUUID(), "Jon D", "jd@example.com");

        repository.save(user1);
        repository.save(user2);

        List<User> found = repository.searchByName("Jon");

        assertTrue(found.contains(user2));
        assertEquals(1, found.size());
    }

    @Test
    void searchUserByName_isCaseInsensitive() {
        User user1 = new User(UUID.randomUUID(), "Gina P", "gp@example.com");
        User user2 = new User(UUID.randomUUID(), "Jon D", "jd@example.com");

        repository.save(user1);
        repository.save(user2);

        List<User> found = repository.searchByName("jon");

        assertEquals(1, found.size());
        assertTrue(found.contains(user2));
    }

    @Test
    void searchUserByName_returnsEmptyListWhenNoMatch() {
        User user = new User(UUID.randomUUID(), "Jon D", "jd@example.com");
        repository.save(user);

        List<User> found = repository.searchByName("xyz");

        assertTrue(found.isEmpty());
    }

    @Test
    void userExistsByEmail_returnsTrueWhenExists() {
        User user = new User(UUID.randomUUID(), "Jon D", "jd@example.com");
        repository.save(user);

        assertTrue(repository.existsByEmail("jd@example.com"));
    }

    @Test
    void userExistsByEmail_returnsFalseWhenNotExists() {
        User user = new User(UUID.randomUUID(), "Jon D", "jd@example.com");
        repository.save(user);

        assertFalse(repository.existsByEmail("suhdf@example.com"));
    }
}
