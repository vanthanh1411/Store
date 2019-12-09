package com.duykhanh.storeapp.network;

import com.duykhanh.storeapp.utils.Constants;
/**
 * Created by Duy Khánh on 11/5/2019.
 * Chứa đường dẫn server và sự kiện nhận, gửi dữ liệu đi
 */
public class ApiUtils {

//    public static final String BASE_URL = Constants.BASE_URL + "/api/str1/";
   public static final String BASE_URL = Constants.BASE_URL + ":4444/api/str1/";

    //Nhận và gửi dữ liệu đi
    public static DataClient getProductList(){
        return RetrofitClient.getClient(BASE_URL).create(DataClient.class);
    }
}
