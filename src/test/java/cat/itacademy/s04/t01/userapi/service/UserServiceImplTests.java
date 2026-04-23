package cat.itacademy.s04.t01.userapi.service;

import cat.itacademy.s04.t01.userapi.exception.UserNotFoundException;
import cat.itacademy.s04.t01.userapi.model.User;
import cat.itacademy.s04.t01.userapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_generatesUuidAndSave() {
        User user = new User();
        user.setName("Anne");
        user.setEmail("anne@example.com");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result.getId());
        assertEquals("Anne", result.getName());

        verify(userRepository).save(user);
    }

    @Test
    void getAllUsers_returnsAllWhenNameIsNull() {
        List<User> users = List.of(
                new User(UUID.randomUUID(), "Anne", "anne@example.com"),
                new User(UUID.randomUUID(), "June", "june@example.com"));

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers(null);

        assertEquals(2, result.size());

        verify(userRepository).findAll();
        verify(userRepository, never()).searchByName(any());
    }

    @Test
    void getAllUsers_returnsFilteredWhenNameProvided() {

        User anne = new User(UUID.randomUUID(), "Anne", "anne@example.com");


        when(userRepository.searchByName("Anne")).thenReturn(List.of(anne));

        List<User> result = userService.getAllUsers("Anne");

        assertEquals(1, result.size());
        assertEquals("Anne", result.getFirst().getName());
        verify(userRepository).searchByName("Anne");
        verify(userRepository, never()).findAll();
    }

    @Test
    void getUserById_returnsUserWhenExists() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Anne", "anne@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getUserById(id);

        assertEquals("Anne", result.getName());
        assertEquals(id, result.getId());
        verify(userRepository).findById(id);
    }

    @Test
    void getUserById_throwsExceptionWhenNotExist() {
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(id));

        verify(userRepository).findById(id);
    }
}