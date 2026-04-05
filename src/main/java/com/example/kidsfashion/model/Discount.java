package com.example.kidsfashion.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "discounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private double percent; // ví dụ 10 = giảm 10%

    private boolean active = true;
}