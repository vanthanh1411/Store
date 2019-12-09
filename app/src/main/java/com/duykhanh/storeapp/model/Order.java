package com.duykhanh.storeapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.duykhanh.storeapp.utils.Formater;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Order implements Parcelable {
    @SerializedName("day")
    @Expose
    Date date;
    String idu;
    String address;
    String phone;
    int status;
    @SerializedName("_id")
    @Expose
    private String ido;
    float total;

    public Order() {
    }

    public Order(Date date, String idu, String address, String phone, float total) {
        this.date = date;
        this.idu = idu;
        this.address = address;
        this.phone = phone;
        this.total = total;
    }

    protected Order(Parcel in) {
        date = Formater.formatStrToDate(in.readString());
        idu = in.readString();
        address = in.readString();
        phone = in.readString();
        status = in.readInt();
        ido = in.readString();
        total = in.readFloat();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIdu() {
        return idu;
    }

    public void setIdu(String idu) {
        this.idu = idu;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIdo() {
        return ido;
    }

    public void setIdo(String ido) {
        this.ido = ido;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Order{" +
                "date=" + date +
                ", idu='" + idu + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", ido='" + ido + '\'' +
                ", total=" + total +
                '}';
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Formater.formatDate(date));
        dest.writeString(idu);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeInt(status);
        dest.writeString(ido);
        dest.writeFloat(total);
    }
}
