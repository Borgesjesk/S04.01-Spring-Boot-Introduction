package cat.itacademy.s04.t01.userapi.service;

import cat.itacademy.s04.t01.userapi.exception.EmailAlreadyExistsException;
import cat.itacademy.s04.t01.userapi.exception.UserNotFoundException;
import cat.itacademy.s04.t01.userapi.model.User;
import cat.itacademy.s04.t01.userapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + user.getEmail());
        }
        user.setId(UUID.randomUUID());
        return repository.save(user);
    }

    @Override
    public List<User> getAllUsers(String name) {
        if (name == null || name.isBlank()) {
            return repository.findAll();
        }
        return repository.searchByName(name);
    }

    @Override
    public User getUserById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }
}