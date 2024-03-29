package com.example.fastdeli.model;

import com.google.firebase.Timestamp;

import java.util.HashMap;

public class Receipt {
    private Timestamp timestamp;

    private Double Total;

    private String productName;

    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Receipt(Timestamp timestamp, Double total, String productName, Integer quantity, String username, HashMap<String, ItemDetails> items, double totalCost) {
        this.timestamp = timestamp;
        Total = total;
        this.productName = productName;
        this.quantity = quantity;
        this.username = username;
        this.items = items;
        this.totalCost = totalCost;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getTotal() {
        return Total;
    }


    public void setTotal(Double total) {
        Total = total;
    }

    private String username;
    private HashMap<String, ItemDetails> items;
    private double totalCost;

    public Receipt() {
        // Default constructor required for Firebase
    }



    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public HashMap<String, ItemDetails> getItems() {
        return items;
    }

    public void setItems(HashMap<String, ItemDetails> items) {
        this.items = items;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "timestamp=" + timestamp +
                ", username='" + username + '\'' +
                ", items=" + items +
                ", totalCost=" + totalCost +
                '}';
    }

    public static class ItemDetails {
        private int quantity;
        private double price;

        public ItemDetails() {
            // Default constructor required for Firebase
        }

        public ItemDetails(int quantity, double price) {
            this.quantity = quantity;
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "ItemDetails{" +
                    "quantity=" + quantity +
                    ", price=" + price +
                    '}';
        }
    }
}
