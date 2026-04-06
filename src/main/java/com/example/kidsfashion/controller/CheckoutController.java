package com.example.kidsfashion.controller;

import com.example.kidsfashion.model.Cart;
import com.example.kidsfashion.model.Discount;
import com.example.kidsfashion.model.Product;
import com.example.kidsfashion.model.User;
import com.example.kidsfashion.repository.UserRepository;
import com.example.kidsfashion.service.DiscountService;
import com.example.kidsfashion.service.OrderService;
import com.example.kidsfashion.service.ProductService;
import com.example.kidsfashion.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VNPayService vnPayService;

    @GetMapping
    public String checkout(HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        Map<Product, Integer> cartItems = new HashMap<>();
        double total = 0;
        double discountAmount = 0;

        for (Map.Entry<Long, Integer> entry : cart.getItems().entrySet()) {
            Product product = productService.getById(entry.getKey());
            if (product != null) {
                cartItems.put(product, entry.getValue());
                total += product.getPrice() * entry.getValue();
            }
        }

        String discountCode = (String) session.getAttribute("discountCode");
        if (discountCode != null && !discountCode.isBlank()) {
            Discount discount = discountService.getByCode(discountCode);
            if (discount != null) {
                discountAmount = total * (discount.getPercent() / 100.0);
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("discountAmount", discountAmount);
        model.addAttribute("finalTotal", total - discountAmount);
        model.addAttribute("activePage", "cart");

        return "checkout";
    }

    @PostMapping("/apply-discount")
    public String applyDiscount(@RequestParam String code,
                                HttpSession session,
                                Model model) {

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        Map<Product, Integer> cartItems = new HashMap<>();
        double total = 0;

        for (Map.Entry<Long, Integer> entry : cart.getItems().entrySet()) {
            Product product = productService.getById(entry.getKey());
            if (product != null) {
                cartItems.put(product, entry.getValue());
                total += product.getPrice() * entry.getValue();
            }
        }

        Discount discount = discountService.getByCode(code != null ? code.trim() : "");
        double discountAmount = 0;

        if (discount != null) {
            discountAmount = total * (discount.getPercent() / 100.0);
            session.setAttribute("discountCode", discount.getCode());
            model.addAttribute("success", "Áp dụng mã giảm giá thành công!");
        } else {
            session.removeAttribute("discountCode");
            model.addAttribute("error", "Mã giảm giá không hợp lệ hoặc đã bị tắt!");
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("discountAmount", discountAmount);
        model.addAttribute("finalTotal", total - discountAmount);
        model.addAttribute("activePage", "cart");

        return "checkout";
    }

    @PostMapping("/confirm")
    public String confirmOrder(@RequestParam String name,
                               @RequestParam String phone,
                               @RequestParam String address,
                               @RequestParam String payment,
                               Authentication authentication,
                               HttpSession session,
                               Model model,
                               HttpServletRequest request) {

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        Cart cart = (Cart) session.getAttribute("cart");

        if (user == null) {
            return "redirect:/login";
        }

        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        String discountCode = (String) session.getAttribute("discountCode");

        double total = 0;
        double discountAmount = 0;

        for (Map.Entry<Long, Integer> entry : cart.getItems().entrySet()) {
            Product product = productService.getById(entry.getKey());
            if (product != null) {
                total += product.getPrice() * entry.getValue();
            }
        }

        if (discountCode != null && !discountCode.isBlank()) {
            Discount discount = discountService.getByCode(discountCode);
            if (discount != null) {
                discountAmount = total * (discount.getPercent() / 100.0);
            }
        }

        double finalTotal = total - discountAmount;
        if (finalTotal < 0) {
            finalTotal = 0;
        }

        try {
            if ("BANK".equalsIgnoreCase(payment)) {
                String paymentUrl = vnPayService.createPaymentUrl(
                        request,
                        Math.round(finalTotal),
                        "Thanh toan don hang KidFashion"
                );

                session.setAttribute("pendingOrder_user", user);
                session.setAttribute("pendingOrder_cart", cart);
                session.setAttribute("pendingOrder_name", name);
                session.setAttribute("pendingOrder_phone", phone);
                session.setAttribute("pendingOrder_address", address);
                session.setAttribute("pendingOrder_payment", payment);
                session.setAttribute("pendingOrder_discountCode", discountCode);

                return "redirect:" + paymentUrl;
            }

            orderService.createOrder(user, cart, name, phone, address, payment, discountCode);

            session.removeAttribute("cart");
            session.removeAttribute("discountCode");

            model.addAttribute("name", name);
            model.addAttribute("payment", payment);
            model.addAttribute("activePage", "user");

            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi đặt hàng: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/vnpay-return")
    public String vnpayReturn(HttpServletRequest request,
                              HttpSession session,
                              Model model) {
        try {
            Map<String, String> fields = new HashMap<>();
            request.getParameterMap().forEach((key, value) -> fields.put(key, value[0]));

            boolean validSignature = vnPayService.verifyReturnUrl(new HashMap<>(fields));
            String responseCode = request.getParameter("vnp_ResponseCode");

            if (validSignature && "00".equals(responseCode)) {
                User user = (User) session.getAttribute("pendingOrder_user");
                Cart cart = (Cart) session.getAttribute("pendingOrder_cart");
                String name = (String) session.getAttribute("pendingOrder_name");
                String phone = (String) session.getAttribute("pendingOrder_phone");
                String address = (String) session.getAttribute("pendingOrder_address");
                String payment = (String) session.getAttribute("pendingOrder_payment");
                String discountCode = (String) session.getAttribute("pendingOrder_discountCode");

                if (user != null && cart != null) {
                    orderService.createOrder(user, cart, name, phone, address, payment, discountCode);
                }

                session.removeAttribute("cart");
                session.removeAttribute("discountCode");

                session.removeAttribute("pendingOrder_user");
                session.removeAttribute("pendingOrder_cart");
                session.removeAttribute("pendingOrder_name");
                session.removeAttribute("pendingOrder_phone");
                session.removeAttribute("pendingOrder_address");
                session.removeAttribute("pendingOrder_payment");
                session.removeAttribute("pendingOrder_discountCode");

                model.addAttribute("name", name);
                model.addAttribute("payment", "BANK");
                model.addAttribute("message", "Thanh toán thành công!");
                model.addAttribute("activePage", "user");

                return "success";
            } else {
                model.addAttribute("error", "Thanh toán thất bại hoặc chữ ký không hợp lệ!");
                return "payment-result";
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi xử lý kết quả thanh toán: " + e.getMessage());
            return "payment-result";
        }
    }
}