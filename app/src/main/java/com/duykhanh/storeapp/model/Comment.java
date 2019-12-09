package com.duykhanh.storeapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Comment {
    @SerializedName("imgc")
    @Expose
    private List<String> imgc = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("idp")
    @Expose
    private String idp;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("point")
    @Expose
    private Integer point;
    @SerializedName("idu")
    @Expose
    private String idu;
    @SerializedName("title")
    @Expose
    private String title;

    public Comment() {
    }

    public Comment(List<String> imgc, String id, String content, String idp, String date, Integer point, String idu, String title) {
        this.imgc = imgc;
        this.id = id;
        this.content = content;
        this.idp = idp;
        this.date = date;
        this.point = point;
        this.idu = idu;
        this.title = title;
    }

    public List<String> getImgc() {
        return imgc;
    }

    public void setImgc(List<String> imgc) {
        this.imgc = imgc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIdp() {
        return idp;
    }

    public void setIdp(String idp) {
        this.idp = idp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getIdu() {
        return idu;
    }

    public void setIdu(String idu) {
        this.idu = idu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "img=" + imgc +
                ", id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", idp='" + idp + '\'' +
                ", date=" + date +
                ", point=" + point +
                ", idu='" + idu + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
