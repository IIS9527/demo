package com.example.demo.Data;

import lombok.Data;

import java.util.List;

@Data
public class TaskData {

    public  String  id;

    public  String  sid;

    public  String  roomAddress;

    public  String  time; //截止时间戳

    public  String  realDieTime; //实际截止时间

    public  String  duration; //时长

    public  String  videoName; //

    public  String  videoNameXiGua; //

    public  String  videoNameTouTiao; //

    public  String  videoDescriptor; //废弃

    public  Integer weight;  // 权重？

    public  Integer number;  //剩余数量

    public  Integer  numberStatic;  //总数量

    public  Integer  numberWorking;  //总数量

    public  String beginTimeFrom;

    public  String beginTimeTo;

    public  String  roomId;  //解析得到

    public  String  state;

    public  Integer  integral;  //积分每分钟

    public  String  mid;  //积分每分钟

    public  String  timeMid;  //时间戳

    public String sign;

    public String personAddress;


    public  Long  creatIntegral;

    public List<DeviceData> deviceDataList;


    public Long getCreatIntegral() {
        if(creatIntegral == null ){
            creatIntegral = 0L;
        }
        return creatIntegral;
    }

    public void setPersonAddress(String personAddress) {
        this.personAddress = personAddress;
    }
}
