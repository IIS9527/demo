package com.example.demo.Data;


import lombok.Data;

import java.math.BigInteger;

@Data
public class ExchangeIntegral {
    private  String exchangeTime;
    private  Long integral;
    private  String cardNo;
    private  String realName;
    private  String qrUrl;
    private  String qrUrlZFB;
    private  String qrUrlOY;
    private  String allowTime;
    private  Integer allowState;

    private  BigInteger id;
    private  Integer page;

    private  Integer size;

    private  Integer pageSize;

    public Integer getPageSize() {
        if (page==null || size == null || size <= 0 || page <= 0 ){
            return null;
        }else {
         pageSize = (page-1)*size;
        }
        return pageSize;
    }

    private String column;

    private String order;

}
