package com.duykhanh.storeapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Duy Khánh on 11/5/2019.
 */
public class Product implements Serializable {

    @SerializedName("img")
    @Expose
    private List<String> img = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("idcategory")
    @Expose
    private String idcategory;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("material")
    @Expose
    private String material;
    @SerializedName("nameproduct")
    @Expose
    private String nameproduct;
    @SerializedName("point")
    @Expose
    private Float point;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("warranty")
    @Expose
    private String warranty;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("promotion")
    @Expose
    private double promotion;
    @SerializedName("view")
    @Expose
    private Integer view;

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdcategory() {
        return idcategory;
    }

    public void setIdcategory(String idcategory) {
        this.idcategory = idcategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public void setNameproduct(String nameproduct) {
        this.nameproduct = nameproduct;
    }

    public float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public double getPromotion() {
        return promotion;
    }

    public void setPromotion(double promotion) {
        this.promotion = promotion;
    }

    public String toString() {
        return "Product{" +
                "img=" + img +
                ", id='" + id + '\'' +
                ", idcategory='" + idcategory + '\'' +
                ", description='" + description + '\'' +
                ", material='" + material + '\'' +
                ", nameproduct='" + nameproduct + '\'' +
                ", point=" + point +
                ", price=" + price +
                ", size='" + size + '\'' +
                ", warranty='" + warranty + '\'' +
                ", quantity=" + quantity +
                '}';
    }

}


