package com.example.kidsfashion.repository;

import com.example.kidsfashion.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // user xem đơn của mình
    List<Order> findByUsernameOrderByCreatedAtDesc(String username);

    // admin xem tất cả đơn
    List<Order> findAllByOrderByCreatedAtDesc();

    // đếm số đơn từ thời điểm nào đó (ví dụ hôm nay)
    long countByCreatedAtAfter(LocalDateTime dateTime);

    // lấy các đơn từ thời điểm nào đó
    List<Order> findByCreatedAtAfter(LocalDateTime dateTime);
}