package com.spike.security.service;

import com.spike.security.model.Product;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @PreFilter("filterObject.owner == authentication.name") // 方法层前置过滤
    public List<Product> sellProducts(List<Product> products) {
        // sell products and return the sold products list
        return products;
    }

    @PostFilter("filterObject.owner == authentication.name") // 方法层后置过滤
    public List<Product> findProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("beer", "nikolai"));
        products.add(new Product("candy", "nikolai"));
        products.add(new Product("chocolate", "julien"));
        return products;
    }
}
