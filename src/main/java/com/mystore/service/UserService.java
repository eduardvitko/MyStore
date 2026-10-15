package com.mystore.service;

import com.mystore.dto.UserRegistrationRequest;
import com.mystore.entity.RoleEnum;
import com.mystore.entity.User;
import com.mystore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Отримати список усіх користувачів
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Знайти користувача за ID
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    // Знайти користувача за username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Знайти користувача за email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Реєстрація нового користувача через DTO
    @Transactional
    public User registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Користувач із таким ім'ям вже існує!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Користувач із таким email вже існує!");
        }

        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(request.getPassword()) // Пізніше додамо сюди шифрування
                .role(RoleEnum.USER)
                .build();

        return userRepository.save(newUser);
    }

    // Видалити користувача за ID
    @Transactional
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Користувача з ID " + id + " не знайдено.");
        }
        userRepository.deleteById(id);
    }
}