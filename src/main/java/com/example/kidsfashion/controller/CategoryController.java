package com.example.kidsfashion.controller;

import com.example.kidsfashion.model.Category;
import com.example.kidsfashion.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository repo;

    // 📋 danh sách
    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", repo.findAll());
        return "categories";
    }

    // ➕ form thêm
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        return "category-form";
    }

    // 💾 save
    @PostMapping("/save")
    public String save(Category category) {
        repo.save(category);
        return "redirect:/admin/categories";
    }

    // ✏️ sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("category", repo.findById(id).orElse(null));
        return "category-form";
    }

    // ❌ xóa
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/admin/categories";
    }
}