package com.example.fastdeli.model;

public class CartProduct {
    public CartProduct() {
    }

    private  String productName;
    private  Double productPrice;

    private String imageCart;

    private Integer quantity;
    private String documentId;

    public String getImageCart() {
        return imageCart;
    }

    public void setImageCart(String imageCart) {
        this.imageCart = imageCart;
    }

    public CartProduct(String productName, Double productPrice, String imageCart, Integer quantity, String documentId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.imageCart = imageCart;
        this.quantity = quantity;
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }





    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public void increaseQuantity() {
        quantity++;
    }

    // Phương thức giảm số lượng
    public void decreaseQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }
}
