package com.duykhanh.storeapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Duy Khánh on 12/4/2019.
 */
public class ProductResponse {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("idp")
    @Expose
    private String idp;
    @SerializedName("total")
    @Expose
    private Integer total;

    @SerializedName("products")
    private List<Product> products;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdp() {
        return idp;
    }

    public void setIdp(String idp) {
        this.idp = idp;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "ProductResponse{" +
                "id='" + id + '\'' +
                ", idp='" + idp + '\'' +
                ", total=" + total +
                '}';
    }
}

