package cat.itacademy.s04.t01.userapi.controller;


import cat.itacademy.s04.t01.userapi.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final List<User> users = new ArrayList<>();

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }
}
