package com.mystore.controller;

import com.mystore.dto.UserRegistrationRequest;
import com.mystore.entity.User;
import com.mystore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")

@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    // Реєстрація нового користувача (підтримує і з слешем, і без)
    @PostMapping({"/register", "/register/"})
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        User registeredUser = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    // Отримати список усіх користувачів
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Знайти користувача за ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }



    // Оновити користувача за ID
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserRegistrationRequest request) {
        User updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    // Видалити користувача за ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    // Ендпоінт для входу за поштою
    @PostMapping({"/login", "/login/"})
    public ResponseEntity<?> loginUser(@RequestBody java.util.Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email не може бути порожнім");
        }

        // Шукаємо користувача за нормалізованою поштою
        Optional<User> userOptional = userService.getUserByEmail(email.trim().toLowerCase());

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Користувача з таким email не знайдено!");
        }
    }
}
