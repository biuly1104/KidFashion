package com.example.kidsfashion.controller;

import com.example.kidsfashion.model.User;
import com.example.kidsfashion.repository.UserRepository;
import com.example.kidsfashion.service.OrderService;
import com.example.kidsfashion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    // =========================
    // LOGIN / REGISTER
    // =========================

    @GetMapping("/login")
    public String loginPage(Model model,
                            Authentication authentication,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout) {

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            return "redirect:/";
        }

        if (error != null) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
        }

        if (logout != null) {
            model.addAttribute("success", "Bạn đã đăng xuất.");
        }

        model.addAttribute("activePage", "user");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            return "redirect:/";
        }

        model.addAttribute("activePage", "user");
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        try {
            userService.register(user);
            model.addAttribute("success", "Đăng ký thành công! Hãy đăng nhập.");
            model.addAttribute("activePage", "user");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("activePage", "user");
            return "register";
        }
    }

    // =========================
    // USER
    // =========================

    @GetMapping("/account")
    public String account(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(authentication.getName()).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("activePage", "user");

        return "account";
    }

    @GetMapping("/orders")
    public String orders(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("orders", orderService.getOrdersByUsername(username));
        model.addAttribute("user", user);
        model.addAttribute("activePage", "user");

        return "orders";
    }

    // =========================
    // ADMIN - DASHBOARD
    // =========================

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalOrders", orderService.getTotalOrders());
        model.addAttribute("totalRevenue", orderService.getTotalRevenue());
        model.addAttribute("todayOrders", orderService.getTodayOrdersCount());
        model.addAttribute("todayRevenue", orderService.getTodayRevenue());

        model.addAttribute("activePage", "user");

        return "admin-dashboard";
    }

    // =========================
    // ADMIN - ĐƠN HÀNG
    // =========================

    @GetMapping("/admin/orders")
    public String adminOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("activePage", "user");
        return "admin-orders";
    }

    @PostMapping("/admin/orders/update-status")
    public String updateOrderStatus(@RequestParam Long id,
                                    @RequestParam String status) {
        orderService.updateStatus(id, status);
        return "redirect:/admin/orders";
    }

    // =========================
    // ADMIN - USER
    // =========================

    @GetMapping("/admin/users")
    public String adminUsers(Model model) {
        model.addAttribute("users", userRepository.findAllByOrderByIdDesc());
        model.addAttribute("activePage", "user");
        return "admin-users";
    }

    @GetMapping("/admin/users/{username}/orders")
    public String adminUserOrders(@PathVariable String username, Model model) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return "redirect:/admin/users";
        }

        model.addAttribute("selectedUser", user);
        model.addAttribute("orders", orderService.getOrdersByUsername(username));
        model.addAttribute("activePage", "user");

        return "admin-user-orders";
    }
}