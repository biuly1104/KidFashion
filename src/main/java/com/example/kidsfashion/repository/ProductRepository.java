package com.example.kidsfashion.repository;

import com.example.kidsfashion.model.Category;
import com.example.kidsfashion.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategory(Category category);

    List<Product> findTop8ByNewestTrue();

    List<Product> findTop8ByBestSellerTrue();

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);
}