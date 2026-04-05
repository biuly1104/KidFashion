package com.example.kidsfashion.service;

import com.example.kidsfashion.model.Cart;
import com.example.kidsfashion.model.Discount;
import com.example.kidsfashion.model.Order;
import com.example.kidsfashion.model.OrderItem;
import com.example.kidsfashion.model.Product;
import com.example.kidsfashion.model.User;
import com.example.kidsfashion.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DiscountService discountService;

    public Order createOrder(User user,
                             Cart cart,
                             String name,
                             String phone,
                             String address,
                             String payment) {
        return createOrder(user, cart, name, phone, address, payment, null);
    }

    public Order createOrder(User user,
                             Cart cart,
                             String name,
                             String phone,
                             String address,
                             String payment,
                             String discountCode) {

        if (user == null) {
            throw new RuntimeException("Bạn cần đăng nhập để đặt hàng!");
        }

        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng đang trống!");
        }

        Order order = new Order();
        order.setCustomerName(name);
        order.setPhone(phone);
        order.setAddress(address);
        order.setPaymentMethod(payment);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("Đã đặt");
        order.setUsername(user.getUsername());

        List<OrderItem> itemList = new ArrayList<>();
        double originalTotal = 0;

        for (Map.Entry<Long, Integer> entry : cart.getItems().entrySet()) {
            Product product = productService.getById(entry.getKey());

            if (product != null) {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProductId(product.getId());
                item.setProductName(product.getName());
                item.setPrice(product.getPrice());
                item.setQuantity(entry.getValue());
                item.setSubtotal(product.getPrice() * entry.getValue());

                originalTotal += item.getSubtotal();
                itemList.add(item);
            }
        }

        double finalTotal = originalTotal;

        if (discountCode != null && !discountCode.trim().isEmpty()) {
            Discount discount = discountService.getByCode(discountCode.trim());

            if (discount != null && discount.isActive()) {
                double discountAmount = originalTotal * (discount.getPercent() / 100.0);
                finalTotal = originalTotal - discountAmount;

                if (finalTotal < 0) {
                    finalTotal = 0;
                }
            }
        }

        order.setItems(itemList);
        order.setTotalAmount(finalTotal);

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order getById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void updateStatus(Long id, String status) {
        Order order = orderRepository.findById(id).orElse(null);

        if (order == null) {
            throw new RuntimeException("Không tìm thấy đơn hàng!");
        }

        order.setStatus(status);
        orderRepository.save(order);
    }

    // =========================
    // DASHBOARD
    // =========================

    public long getTotalOrders() {
        return orderRepository.count();
    }

    public double getTotalRevenue() {
        List<Order> orders = orderRepository.findAll();
        double total = 0;

        for (Order order : orders) {
            total += order.getTotalAmount();
        }

        return total;
    }

    public long getTodayOrdersCount() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        return orderRepository.countByCreatedAtAfter(startOfDay);
    }

    public double getTodayRevenue() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        List<Order> todayOrders = orderRepository.findByCreatedAtAfter(startOfDay);

        double total = 0;
        for (Order order : todayOrders) {
            total += order.getTotalAmount();
        }

        return total;
    }
}