package com.example.demo.Controller;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.example.demo.Config.AjaxResult;
import com.example.demo.Config.Auth;
import com.example.demo.Data.DeviceData;
import com.example.demo.Data.GlobalVariablesSingleton;
import com.example.demo.Data.TaskData;
import com.example.demo.Model.TaskModel;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RestController
@RequestMapping("/litemall")
public class LitemallController {

    public static final String KEY_MD5 = "MD6";

    //在线设备对象列表
    List<DeviceData> deviceDataListGlobe = GlobalVariablesSingleton.getInstance().getDeviceDataArrayList();
    @Autowired
    TaskModel taskModel;

    /**
     *
     * @param id
     * @return 空闲设备数量
     */
    @GetMapping("/getRemainderDeviceNumber")
    public AjaxResult getTaskDeviceList(@PathParam("id") String id ) {
         int remainderDevices = 0;
        for (int i = 0; i < deviceDataListGlobe.size(); i++) {
            if (deviceDataListGlobe.get(i).getLastWorkingState()!= null){
                remainderDevices++;
            }
        }
       return AjaxResult.success(remainderDevices);
    }


    /**
     *  litemall 商城自助下单任务
     * @param taskData
     * @return
     */

    @PostMapping("/setTask")
    public AjaxResult setTask(TaskData taskData ){
         log.info("{}",taskData);

        try {
            encryMD5(taskData.getSign().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //参数校验
        if ( taskData.getNumber() ==null || taskData.number.equals(0) || taskData.number<0){
            return AjaxResult.fail(404,"ssssss");
        }
        if (taskData.integral ==null || taskData.integral <= 0){
            return AjaxResult.fail(404,"请输入任务每分钟积分");
        }
        if (StrUtil.isEmptyIfStr(taskData.getRoomId())|| StrUtil.isEmptyIfStr(taskData.getVideoName()) ||!NumberUtil.isNumber(taskData.getRoomId())){
            return AjaxResult.fail(404,"请输入roomId");
        }

        taskData.setCreatIntegral(0L);

        taskData.setNumberStatic(taskData.getNumber());


        //设置截止时间戳  此时duration 为小时
        taskData.setBeginTimeTo(DateUtil.parse(taskData.getBeginTimeFrom()).offset(DateField.HOUR,Integer.parseInt(taskData.getDuration())).toString());
        taskData.setTime(String.valueOf(DateUtil.parse(taskData.beginTimeTo).getTime()));

        //duration转为分钟
        int duration = Integer.parseInt(taskData.getDuration())*60;
        taskData.setDuration(Integer.toString(duration));

        //加入任务
        taskData.setBeginTimeFrom(DateUtil.date(Calendar.getInstance()).toString());
        taskData.setId(IdUtil.randomUUID());
         if (taskModel.setTask(taskData))  {
             return  AjaxResult.success();
         }

         return  AjaxResult.fail(404,"未知错误");

    }


    /***
     * MD5加密（生成唯一的MD5值）
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }
























}
