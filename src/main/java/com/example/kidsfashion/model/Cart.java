package com.example.kidsfashion.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private Map<Long, Integer> items = new HashMap<>();

    public Map<Long, Integer> getItems() {
        return items;
    }

    public void add(Long productId) {
        items.put(productId, items.getOrDefault(productId, 0) + 1);
    }

    public void increase(Long productId) {
        add(productId);
    }

    public void decrease(Long productId) {
        if (items.containsKey(productId)) {
            int qty = items.get(productId);
            if (qty <= 1) {
                items.remove(productId);
            } else {
                items.put(productId, qty - 1);
            }
        }
    }

    public void remove(Long productId) {
        items.remove(productId);
    }
}