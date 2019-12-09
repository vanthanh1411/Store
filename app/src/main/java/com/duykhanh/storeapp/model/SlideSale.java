package com.duykhanh.storeapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Duy Khánh on 12/7/2019.
 */
public class SlideSale {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("products")
    @Expose
    private IdProduct products;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("productObjects")
    @Expose
    private Product productObjects;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public IdProduct getProducts() {
        return products;
    }

    public void setProducts(IdProduct products) {
        this.products = products;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Product getProductObjects() {
        return productObjects;
    }

    public void setProductObjects(Product productObjects) {
        this.productObjects = productObjects;
    }

}
