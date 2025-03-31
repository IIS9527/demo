package com.example.demo.Model;


import com.example.demo.Data.DeviceData;
import com.example.demo.Data.GlobalVariablesSingleton;
import com.example.demo.Data.TaskData;
import com.example.demo.Mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LitemallModel {
    List<DeviceData> deviceDataList = GlobalVariablesSingleton.getInstance().getDeviceDataArrayList();

    @Autowired
    TaskMapper taskMapper;

    public List<TaskData>  getTempTaskByEndTime(Date endTime){
        return taskMapper.selectTempTaskByEndTime(endTime);
    }


}
