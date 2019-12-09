package com.duykhanh.storeapp.utils;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formater {
    final String TAG = this.getClass().toString();

    public static String formatDate(Date date) {
        String dateString = "";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static Date formatStrToDate(String dateStr) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatMoney(int money) {
        DecimalFormat formatter = new DecimalFormat("###,###,###,###");
        return formatter.format(money);

    }

    public static String formatImageLink(String inputLink) {

//        return "https://strdecor.herokuapp.com/" + inputLink.substring(22);
        return  Constants.BASE_URL + inputLink.substring(16);
    }

    public static String statusIntToString(int orderStatusInt) {
        String orderStatusStr = "";
        switch (orderStatusInt) {
            case 0:
                orderStatusStr = "Đơn hàng đang chờ đơn vị xử lý";
                break;
            case 1:
                orderStatusStr = "Đơn hàng đã được tiếp nhận";
                break;
            case 2:
                orderStatusStr = "Đơn hàng đã được bàn giao cho đơn vị vận chuyển";
                break;
            case 3:
                orderStatusStr = "Đơn hàng đang được vận chuyển";
                break;
            case 4:
                orderStatusStr = "Đơn hàng đã vận chuyển thành công";
                break;
        }
        return orderStatusStr;
    }

    public String formatNameProduct(String nameProduct) {
        if (nameProduct.length() > 45) {
            String formatStringProduct = nameProduct.substring(45);
            String nameProductCut = nameProduct.replace(formatStringProduct, "...");
            return nameProductCut;
        }
        return nameProduct + "\n";
    }

    public String formatNameProductView(String nameProduct) {
        if (nameProduct.length() > 22) {
            String formatStringProduct = nameProduct.substring(22);
            String nameProductCut = nameProduct.replace(formatStringProduct, "...");
            return nameProductCut;
        }

        return nameProduct;
    }

    public String formatNameProductViewMore(String nameProduct) {
        if (nameProduct.length() > 40) {
            String formatStringProduct = nameProduct.substring(40);
            String nameProductCut = nameProduct.replace(formatStringProduct, "...");
            return nameProductCut;
        }
        return nameProduct;
    }
}
