package com.example.fastdeli.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> productList;

    public Cart() {
        productList = new ArrayList<>();
    }

    public void addProduct(Product product) {
        productList.add(product);
    }

    public void removeProduct(Product product) {
        productList.remove(product);
    }

    public List<Product> getProductList() {
        return productList;
    }
}