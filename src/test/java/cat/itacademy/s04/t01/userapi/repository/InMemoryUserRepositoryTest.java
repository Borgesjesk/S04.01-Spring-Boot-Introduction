package cat.itacademy.s04.t01.userapi.repository;

import cat.itacademy.s04.t01.userapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    @Test
    void save_addsUserToList() {
        User user = new User(UUID.randomUUID(), "Ted Smith", "td@example.com");
        repository.save(user);

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void save_returnsSavedUser() {
        User user = new User(UUID.randomUUID(), "Lana Dana", "ld@example.com");

        User saved =  repository.save(user);

        assertEquals("Lana Dana", saved.getName());
        assertEquals("ld@example.com", saved.getEmail());
    }
}
