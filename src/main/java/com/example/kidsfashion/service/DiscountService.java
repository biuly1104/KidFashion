package com.example.kidsfashion.service;

import com.example.kidsfashion.model.Discount;
import com.example.kidsfashion.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository repo;

    public List<Discount> getAll() {
        return repo.findAll();
    }

    public void save(Discount discount) {
        repo.save(discount);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Discount getByCode(String code) {
        Optional<Discount> discount = repo.findByCodeAndActiveTrue(code);
        return discount.orElse(null);
    }
}