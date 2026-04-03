package com.example.kidsfashion.service;

import com.example.kidsfashion.model.Product;
import com.example.kidsfashion.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Lấy tất cả sản phẩm
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    // Lấy theo ID
    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Lưu sản phẩm (thêm/sửa)
    public Product save(Product product) {
        return productRepository.save(product);
    }

    // Xóa sản phẩm
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    // Tìm kiếm theo tên
    public List<Product> search(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    // Sản phẩm mới nhất (admin chọn thủ công)
    public List<Product> getLatestProducts() {
        return productRepository.findTop8ByNewestTrue();
    }

    // Sản phẩm bán chạy (admin chọn thủ công)
    public List<Product> getBestSellingProducts() {
        return productRepository.findTop8ByBestSellerTrue();
    }
}