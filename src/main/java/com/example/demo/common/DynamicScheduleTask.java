package com.example.demo.common;


import cn.hutool.Hutool;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.example.demo.Data.DeviceData;
import com.example.demo.Data.GlobalVariablesSingleton;
import com.example.demo.Data.TaskData;
import com.example.demo.Data.User;
import com.example.demo.Mapper.IntegralMapper;
import com.example.demo.Mapper.TaskMapper;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Model.TaskModel;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class DynamicScheduleTask  {

 List<TaskData> taskDataList = GlobalVariablesSingleton.getInstance().getTaskDataArrayList();

    //在线设备对象列表
 List<DeviceData> deviceDataListGlobe = GlobalVariablesSingleton.getInstance().getDeviceDataArrayList();

 List<User> userListGlobal = GlobalVariablesSingleton.getInstance().getUsers();
    @Autowired
    TaskModel taskModel;
    @Autowired
    UserMapper userMapper;
    @Autowired
    TaskMapper taskMapper;

    @Scheduled(cron ="*/20 * * * * ?")
    @Transactional
    public void pushMessage() {


        Long currentTime = System.currentTimeMillis();
        log.info("检查过期任务 ");
        for (int i = 0; i < taskDataList.size(); i++) {

            if (currentTime > Long.parseLong(taskDataList.get(i).getTime())){

                log.info("删除过期任务");

                taskModel.deleteTaskById(taskDataList.get(i).getId());

            }
        }


        for (int i = 0; i < deviceDataListGlobe.size() ; i++) {

            if (currentTime > deviceDataListGlobe.get(i).getState()+1000*85){ // 判断设备是否掉线 //如果 掉线
                //判断是否是在线任务
//                log.info("掉线{}",deviceDataListGlobe.get(i));
                for (int j = 0; j < taskDataList.size(); j++){

                    //roomId在任务列表中  即任务还未截止 脚本掉线

                    if (!StrUtil.isEmptyIfStr(deviceDataListGlobe.get(i).getId()) && deviceDataListGlobe.get(i).getId().equals(taskDataList.get(j).getId())){
                        // 记录数据
                        taskMapper.insertDeviceDataOnce(deviceDataListGlobe.get(i));

                    // 清除设备任务 设置roomId为0
                        deviceDataListGlobe.get(i).setRoomId("0");

                    // 清除设备当前任务 设置为0
                        deviceDataListGlobe.get(i).setId("0");

                    // 收回任务设备数
                        taskDataList.get(j).setNumber(taskDataList.get(j).getNumber()+1);

//                        log.info("记录设备数{}",taskDataList.get(j).getNumber());
                         continue;
                    }

                }

            }

            if( !StrUtil.isEmptyIfStr(deviceDataListGlobe.get(i).getHaveWorkTime())   &&
                    currentTime>deviceDataListGlobe.get(i).getHaveWorkTime()+1000*240  &&
                    StrUtil.isEmptyIfStr( deviceDataListGlobe.get(i).getLastWorkingState()) ){//设备领取任务4分钟后还未进入任务中

                for (int j = 0; j < taskDataList.size(); j++){

                    //roomId在任务列表中  即任务还未截止 脚本掉线

                    if (!StrUtil.isEmptyIfStr(deviceDataListGlobe.get(i).getId()) && deviceDataListGlobe.get(i).getId().equals(taskDataList.get(j).getId())){

                        // 记录数据
                        taskMapper.insertDeviceDataOnce(deviceDataListGlobe.get(i));

                        // 清除设备任务 设置roomId为0
                        deviceDataListGlobe.get(i).setRoomId("0");

                        deviceDataListGlobe.get(i).setId("0");

                        // 收回任务设备数
                        taskDataList.get(j).setNumber(taskDataList.get(j).getNumber()+1);

//                        log.info("记录设备数{}",taskDataList.get(j).getNumber());
                        continue;

                    }

                }

            }



        }

        //增加新任务
        List<TaskData> tempTaskDataList =  taskMapper.selectAllTempTask();

        for (int i = 0; i < tempTaskDataList.size(); i++) {

            log.info("creat a task，{} ",DateUtil.between(DateUtil.parse(tempTaskDataList.get(i).getBeginTimeFrom()),DateUtil.date(), DateUnit.MS));
            if (DateUtil.compare(DateUtil.parse(tempTaskDataList.get(i).getBeginTimeFrom()),DateUtil.date())<1){
                log.info("creat a task now");
                //先用temp表id删除指定缓存任务
                taskMapper.deleteTempTask(tempTaskDataList.get(i));
//                //再注入新的uid
//                tempTaskDataList.get(i).setId(IdUtil.randomUUID());
                taskModel.setTask(tempTaskDataList.get(i));
            }
        }





    }



    // everyday zero todo
    @Scheduled(cron ="0 0 0 * * ?")
    @Transactional
    public void updateUser() {
    // updata tempIntegral

        Long now = System.currentTimeMillis();
        userMapper.updataTempIntegralEveryday();
    //清除每日设备领取任务数
        for (int i = deviceDataListGlobe.size() -1; i >=0; i--) {
            deviceDataListGlobe.get(i).setTodayTaskIntegral(0l);
            deviceDataListGlobe.get(i).setTodayTaskNumber(0);
            //清除过久没登录的设备
            if (deviceDataListGlobe.get(i).getState()+24*3600*1000<now){
                deviceDataListGlobe.remove(i);
            }
        }

        userListGlobal = new ArrayList<User>();

    }






}
