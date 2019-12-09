package com.duykhanh.storeapp.model;

/**
 * Created by Duy Khánh on 12/3/2019.
 */
public class ResponseComment {
    private String result_code;
    private String messsage;

    public ResponseComment(String result_code, String messsage) {
        this.result_code = result_code;
        this.messsage = messsage;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getMesssage() {
        return messsage;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }
}
