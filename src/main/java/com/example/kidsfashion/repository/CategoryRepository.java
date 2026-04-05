package com.example.kidsfashion.repository;

import com.example.kidsfashion.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}