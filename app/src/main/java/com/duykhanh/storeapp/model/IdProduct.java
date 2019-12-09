package com.duykhanh.storeapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Duy Khánh on 12/7/2019.
 */
public class IdProduct {
    @SerializedName("promotion")
    @Expose
    private Double promotion;
    @SerializedName("_id")
    @Expose
    private String id;

    public Double getPromotion() {
        return promotion;
    }

    public void setPromotion(Double promotion) {
        this.promotion = promotion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
