package com.mystore.service;

import com.mystore.dto.UserRegistrationRequest;
import com.mystore.entity.RoleEnum;
import com.mystore.entity.User;
import com.mystore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder; // Імпорт енкодера
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Додаємо залежність для шифрування

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

    // Знайти користувача за email (з нормалізацією)
    public Optional<User> getUserByEmail(String email) {
        if (email == null) return Optional.empty();
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    // Реєстрація нового користувача через DTO
    @Transactional
    public User registerUser(UserRegistrationRequest request) {
        // Нормалізуємо email (приводимо до нижнього регістру і прибираємо пробіли)
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        String normalizedUsername = request.getUsername().trim();

        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new IllegalArgumentException("Користувач із таким ім'ям вже існує!");
        }
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Користувач із таким email вже існує!");
        }

        User newUser = User.builder()
                .username(normalizedUsername)
                .email(normalizedEmail)
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword())) // Шифруємо пароль!
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

    // Оновити дані користувача за ID
    @Transactional
    public User updateUser(Integer id, UserRegistrationRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Користувача з ID " + id + " не знайдено."));

        String normalizedEmail = request.getEmail().trim().toLowerCase();
        String normalizedUsername = request.getUsername().trim();

        if (!existingUser.getUsername().equals(normalizedUsername) &&
                userRepository.existsByUsername(normalizedUsername)) {
            throw new IllegalArgumentException("Користувач із таким ім'ям вже існує!");
        }

        if (!existingUser.getEmail().equals(normalizedEmail) &&
                userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Користувач із таким email вже існує!");
        }

        existingUser.setUsername(normalizedUsername);
        existingUser.setEmail(normalizedEmail);
        existingUser.setPhone(request.getPhone());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existingUser.setPasswordHash(passwordEncoder.encode(request.getPassword())); // Шифруємо новий пароль
        }

        return userRepository.save(existingUser);
    }
}