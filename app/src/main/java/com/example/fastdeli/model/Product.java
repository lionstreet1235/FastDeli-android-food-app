package com.example.fastdeli.model;



public class Product {
    private String name;
    private Double cost;
    private String detail;
    private String image;
    private int quantity;

    public Product() {
    }

    public Product(String name, Double cost, String detail, String image) {
        this.name = name;
        this.cost = cost;
        this.detail = detail;
        this.image = image;

    }

    public Product(String productName, double productPrice) {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
