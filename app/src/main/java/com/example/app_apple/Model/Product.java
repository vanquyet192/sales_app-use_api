package com.example.app_apple.Model;

public class Product {
    private int id;
    private String title;
    private double price;
    private String description;
    private String imageUrl;


    public Product(int id, String title, double price, String description, String imageUrl) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl; // Correctly set the imageUrl field
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
    public String getImageUrl() {
        return imageUrl;
    }
}