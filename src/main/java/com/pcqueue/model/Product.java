package com.pcqueue.model;

import com.google.gson.Gson;

public class Product {

    private int id;
    private String productname;
    private double price;

    public Product(int id, String productname, double price) {
        this.id = id;
        this.productname = productname;
        this.price = price;
    }

    public Product(String json) {
        Gson gson = new Gson();
        Product tempProduct = gson.fromJson(json, Product.class);
        this.id = tempProduct.id;
        this.productname = tempProduct.productname;
        this.price = tempProduct.price;
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
