package com.example.fastdeli.model;

public class CartProduct {
    public CartProduct() {
    }

    private  String productName;
    private  Double productPrice;

    private String image;

    private Integer quantity;
    private String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public CartProduct(String productName, Double productPrice, String image, Integer quantity, String documentId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.image = image;
        this.quantity = quantity;
        this.documentId = documentId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
