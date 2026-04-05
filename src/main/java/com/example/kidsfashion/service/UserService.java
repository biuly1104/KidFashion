package com.example.kidsfashion.service;

import com.example.kidsfashion.model.User;
import com.example.kidsfashion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Đăng ký
    public void register(User user) {

        if (user == null) {
            throw new RuntimeException("Dữ liệu không hợp lệ!");
        }

        String username = user.getUsername().trim();
        String password = user.getPassword().trim();

        if (username.isEmpty()) {
            throw new RuntimeException("Username không được trống!");
        }

        if (password.isEmpty()) {
            throw new RuntimeException("Mật khẩu không được trống!");
        }

        if (repo.existsByUsername(username)) {
            throw new RuntimeException("Username đã tồn tại!");
        }

        user.setUsername(username);

        // 🔥 mã hóa chuẩn
        user.setPassword(passwordEncoder.encode(password));

        // 🔥 mặc định USER
        if (user.getRole() == null) {
            user.setRole("USER");
        }

        repo.save(user);
    }
}