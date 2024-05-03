package com.example.demo.Model;

import com.example.demo.Data.DeviceData;
import com.example.demo.Data.GlobalVariablesSingleton;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class DevicesModel {
    List<DeviceData> deviceDataList = GlobalVariablesSingleton.getInstance().getDeviceDataArrayList();

    public List<DeviceData> findDevices(String cardNo,String state ){

        Long now = System.currentTimeMillis();

        List<DeviceData> userDeviceDataList = new CopyOnWriteArrayList<>();

        //全部设备
        if (state.equals("all") ){
            deviceDataList.stream().parallel().forEach( deviceData -> {
                        if (cardNo.equals(deviceData.getCardNo())){
                            userDeviceDataList.add(deviceData);
                        }
                    }
            );

        }

        //在线设备
        if (state.equals("online")){
            deviceDataList.stream().parallel().forEach( deviceData -> {
                        if (cardNo.equals(deviceData.getCardNo())&& deviceData.getState()+1000*100 > now){
                            userDeviceDataList.add(deviceData);
                        }
                    }
            );
        }
        //任务中
        if (state.equals("working")){
            deviceDataList.stream().parallel().forEach( deviceData -> {
                        if (cardNo.equals(deviceData.getCardNo())&& deviceData.getState()+1000*100 > now && !deviceData.getRoomId().isEmpty() ){
                            userDeviceDataList.add(deviceData);
                        }
                    }
            );
        }

        //离线设备
        if (state.equals("offline")){
            deviceDataList.stream().parallel().forEach( deviceData -> {
                        if (cardNo.equals(deviceData.getCardNo())&& deviceData.getState()+1000*100 < now){
                            userDeviceDataList.add(deviceData);
                        }
                    }
            );
        }



       return userDeviceDataList;


    }


}
