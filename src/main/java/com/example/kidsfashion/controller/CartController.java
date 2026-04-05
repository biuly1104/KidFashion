package com.example.kidsfashion.controller;

import com.example.kidsfashion.model.Cart;
import com.example.kidsfashion.model.Product;
import com.example.kidsfashion.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ProductService productService;

    private Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @PostMapping("/add/{id}")
    public String add(@PathVariable Long id,
                      HttpSession session,
                      HttpServletRequest request) {
        Cart cart = getCart(session);
        cart.add(id);

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            return "redirect:" + referer;
        }

        return "redirect:/products";
    }

    @GetMapping
    public String view(Model model, HttpSession session) {
        Cart cart = getCart(session);

        Map<Product, Integer> cartItems = new HashMap<>();
        double total = 0;

        for (Map.Entry<Long, Integer> entry : cart.getItems().entrySet()) {
            Product product = productService.getById(entry.getKey());
            if (product != null) {
                cartItems.put(product, entry.getValue());
                total += product.getPrice() * entry.getValue();
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("activePage", "cart");

        return "cart";
    }

    @GetMapping("/increase/{id}")
    public String increase(@PathVariable Long id, HttpSession session) {
        Cart cart = getCart(session);
        cart.increase(id);
        return "redirect:/cart";
    }

    @GetMapping("/decrease/{id}")
    public String decrease(@PathVariable Long id, HttpSession session) {
        Cart cart = getCart(session);
        cart.decrease(id);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id, HttpSession session) {
        Cart cart = getCart(session);
        cart.remove(id);
        return "redirect:/cart";
    }
}