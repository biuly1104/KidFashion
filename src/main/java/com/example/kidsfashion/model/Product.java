package com.example.kidsfashion.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private double price;

    private String description;

    private String image;

    // ⭐ NEW
    private boolean newest;

    // 🔥 BEST SELLER
    private boolean bestSeller;
}