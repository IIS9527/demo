package com.example.demo.Data;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class DeviceTaskData {
    private String deviceId;
    private String deviceNickName;
    private String cardNo;
    private String personName;
    private Long startTime;
    private Long startWorkTime;
    private Long lastWorkTime;
    private Long duration;

}
