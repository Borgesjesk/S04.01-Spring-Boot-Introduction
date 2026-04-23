package cat.itacademy.s04.t01.userapi.controller;

import cat.itacademy.s04.t01.userapi.model.User;
import cat.itacademy.s04.t01.userapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> getAllUsers(@RequestParam(required = false) String name) {
        return service.getAllUsers(name);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(service.createUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        return new ResponseEntity<>(service.getUserById(id), HttpStatus.OK);
    }
}