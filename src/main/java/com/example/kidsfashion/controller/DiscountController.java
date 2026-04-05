package com.example.kidsfashion.controller;

import com.example.kidsfashion.model.Discount;
import com.example.kidsfashion.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("discounts", discountService.getAll());
        return "admin/discounts";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("discount", new Discount());
        return "admin/discount-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Discount discount) {
        discountService.save(discount);
        return "redirect:/admin/discounts";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        discountService.delete(id);
        return "redirect:/admin/discounts";
    }
}