package com.example.kidsfashion.controller;

import com.example.kidsfashion.model.Category;
import com.example.kidsfashion.model.Product;
import com.example.kidsfashion.repository.CategoryRepository;
import com.example.kidsfashion.repository.ProductRepository;
import com.example.kidsfashion.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    // 🏠 Trang chủ
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("latestProducts", productService.getLatestProducts());
        model.addAttribute("bestSellingProducts", productService.getBestSellingProducts());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("activePage", "home");
        return "home";
    }

    // 🛍️ Danh sách sản phẩm + tìm kiếm + lọc theo loại + phân trang
    @GetMapping("/products")
    public String viewProducts(Model model,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "categoryId", required = false) Long categoryId,
                               @RequestParam(value = "page", defaultValue = "0") int page) {

        int size = 8; // mỗi trang 8 sản phẩm
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage;
        Category selectedCategory = null;

        if (categoryId != null) {
            selectedCategory = categoryRepository.findById(categoryId).orElse(null);
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            productPage = productRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
        } else if (selectedCategory != null) {
            productPage = productRepository.findByCategory(selectedCategory, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        model.addAttribute("listProducts", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("activePage", "products");

        return "products";
    }

    // ➕ Form thêm sản phẩm
    @GetMapping("/products/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("activePage", "products");
        return "add";
    }

    // 💾 Lưu (add + edit) + upload ảnh từ máy
    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "fileImage", required = false) MultipartFile fileImage,
                              @RequestParam(value = "oldImage", required = false) String oldImage) {

        try {
            if (fileImage != null && !fileImage.isEmpty()) {
                String uploadDir = "uploads/";
                String originalName = fileImage.getOriginalFilename();
                String cleanName = originalName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                String fileName = System.currentTimeMillis() + "_" + cleanName;

                Path uploadPath = Paths.get(uploadDir);
                Files.createDirectories(uploadPath);

                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, fileImage.getBytes());

                product.setImage("/uploads/" + fileName);
            } else {
                product.setImage(oldImage);
            }

            productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/products";
    }

    // ❌ Xóa
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productRepository.deleteById(id);
        return "redirect:/products";
    }

    // ✏️ Form sửa
    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("activePage", "products");
        return "edit";
    }

    // 🔥 Khuyến mãi
    @GetMapping("/discounts")
    public String discounts(Model model) {
        model.addAttribute("activePage", "discounts");
        return "discounts";
    }

    // 🔎 Chi tiết sản phẩm
    @GetMapping("/product/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product p = productService.getById(id);
        model.addAttribute("product", p);
        model.addAttribute("activePage", "products");
        return "product-detail";
    }

    // 👤 Người dùng
    @GetMapping("/user")
    public String user(Model model) {
        model.addAttribute("activePage", "user");
        return "user";
    }
}