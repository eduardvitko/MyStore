package com.mystore.repository;

import com.mystore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Додатковий метод, щоб перевіряти, чи існує категорія з такою назвою
    boolean existsByName(String name);

    Optional<Category> findByName(String name);
}