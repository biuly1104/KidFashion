package com.example.kidsfashion.repository;

import com.example.kidsfashion.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<Discount> findByCodeAndActiveTrue(String code);
}