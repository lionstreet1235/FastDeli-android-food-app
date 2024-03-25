package com.example.fastdeli.model;

import com.google.firebase.Timestamp;

import java.util.HashMap;

public class Receipt {
    private Timestamp time;
    private String username;
    private HashMap<String, ItemDetails> items;
    private double totalCost;

    public Receipt() {
        // Default constructor required for Firebase
    }

    public Receipt(Timestamp time, String username) {
        this.time = time;
        this.username = username;
        this.items = new HashMap<>();
        this.totalCost = 0.0;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
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

    public void addItem(String itemName, int quantity, double price) {
        ItemDetails itemDetails = new ItemDetails(quantity, price);
        items.put(itemName, itemDetails);
        totalCost += quantity * price;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "time=" + time +
                ", username='" + username + '\'' +
                ", items=" + items +
                ", totalCost=" + totalCost +
                '}';
    }

    public static class ItemDetails {
        private int quantity;
        private double price;

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
