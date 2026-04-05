package com.example.kidsfashion.controller;

import com.example.kidsfashion.model.Cart;
import com.example.kidsfashion.model.User;
import com.example.kidsfashion.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/payment/vnpay-return")
    public String paymentReturn(HttpSession session) {

        String responseCode = (String) session.getAttribute("vnp_ResponseCode");

        if ("00".equals(responseCode)) {

            User user = (User) session.getAttribute("pendingOrder_user");
            Cart cart = (Cart) session.getAttribute("pendingOrder_cart");

            String[] info = (String[]) session.getAttribute("pendingOrder_info");

            orderService.createOrder(
                    user,
                    cart,
                    info[0],
                    info[1],
                    info[2],
                    info[3],
                    info[4]
            );

            session.removeAttribute("cart");
            session.removeAttribute("discountCode");

            return "success";
        }

        return "fail";
    }
}