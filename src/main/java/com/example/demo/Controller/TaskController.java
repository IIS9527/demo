package com.example.demo.Controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import com.example.demo.Address.XiguaAddress;
import com.example.demo.Config.AjaxResult;
import com.example.demo.Config.ApplicationVariable;
import com.example.demo.Config.Auth;
import com.example.demo.Config.File;
import com.example.demo.Data.*;
import com.example.demo.Mapper.TaskMapper;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Model.TaskModel;
import com.example.demo.common.IpUtil;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping("/Task")
public class TaskController {
@Autowired
    XiguaAddress xiguaAddress;
@Autowired
    TaskMapper taskMapper;
@Autowired
    TaskModel  taskModel;
@Autowired
    UserMapper userMapper;
@Autowired
    File file;
@Autowired
    IpUtil ipUtil;

//在线任务列表
List<TaskData> taskDataList = GlobalVariablesSingleton.getInstance().getTaskDataArrayList();

//在线设备对象列表
List<DeviceData> deviceDataList = GlobalVariablesSingleton.getInstance().getDeviceDataArrayList();

//用户列表
List<User> userListGlobal = GlobalVariablesSingleton.getInstance().getUsers();

//线程锁
boolean lock = false;

    @GetMapping("/getTask")
    public AjaxResult getTask(@PathParam("cardNo") String cardNo, @PathParam("personName") String personName, @PathParam("time") String time,
                              @PathParam("deviceId") String deviceId, @PathParam("deviceNickName") String deviceNickName,
                              @PathParam("mid") String mid,@PathParam("roomId") String roomId,@PathParam("id") String id,
                               HttpServletRequest httpServletRequest){


        Long timeNow = System.currentTimeMillis();

        //1校验数据md5
//        log.info("sssssssssssssss?????");

        String md5 = SecureUtil.md5(cardNo+personName+time+deviceId+deviceNickName+"sb1314520sbNB$HHHH");

        log.info("md5:{},cardNo:{},personName:{},time:{},deviceId:{},deviceNickName:{},mid:{}",md5,cardNo,personName,time,deviceId,deviceNickName,mid);

          if (!md5.equals(mid)){

              log.info("sssssssssssssss?????");

              return  AjaxResult.fail(-1,"?????你在做什么,唱歌");

          }




        if(!(timeNow+10000>=Long.parseLong(time)&&(timeNow-10*1000)<Long.parseLong(time))){
            log.info("timeErro：{}，{}",timeNow,time);
            return  AjaxResult.fail(-1,"?????你在做什么,唱歌");
        }


        //脚本请求接受任务

        //更新设备状态

        int has = 0; //设备是否 在设备列表

        int deviceIndex = 0; //记录设备在设备列表的索引

        TaskData  taskData = null; //要分配的任务

        boolean hasUser = false;//设备用户是否在用户列表中

        for (int i = 0; i < userListGlobal.size(); i++) {

            if (userListGlobal.get(i).getCardNo().equals(cardNo) && userListGlobal.get(i).getState().equals(1)){  //在账户中 且账户状态正常

                hasUser =true;

                break;

            }
        }

        if (!hasUser && !lock  ){

            //当前设备  无用户拥有

            // 从数据库里取出用户信息 存入用户列表

            //锁住
                lock =true;

                User user = userMapper.selectMyInfoByCardNo(cardNo);

                //初始化用户 不需要缓存积分
                user.setTempIntegral(0L);

                log.info("addUserList:{}",user);

                if (user == null || user.getState().equals(0) ){

                    return AjaxResult.fail(-1,"请先注册，或填写正确用户名");

                }

            boolean insite = true;

            for (User value : userListGlobal) {
                if (user.getCardNo().equals(value.getCardNo())) {
                    insite = false;
                    break;
                }
            }
            if (insite){
                userListGlobal.add(user);
            }
           lock =false;
        }

        //是否已在 在线设备列表
        for (int i=0;i<deviceDataList.size();i++){

            //设备再设备列表中
            if (deviceDataList.get(i).getDeviceId().equals(deviceId)){  //设备在列表中

                has = 1;

                deviceIndex = i;

                //更新状态时间
                deviceDataList.get(i).setState(System.currentTimeMillis());
                deviceDataList.get(i).setDeviceNickName(deviceNickName);
                deviceDataList.get(i).setPersonName(personName);
//              log.info("realIp:{}",httpServletRequest.getRemoteAddr());
                deviceDataList.get(i).setCardNo(cardNo);
                deviceDataList.get(i).setIp( ipUtil.getIpAddr3(httpServletRequest));
                deviceDataList.get(i).setHaveWorkTime(timeNow);
                deviceDataList.get(i).setLastWorkingState(null);


                //从任务列表查出发送
//                for (int j=0;j<taskDataList.size();j++){

//                    if (taskDataList.get(j).getRoomId().equals(deviceDataList.get(i).getRoomId()) && !taskDataList.get(j).getRoomId().equals(roomId) && !roomId.equals("0")){ //已接任务
//                        log.info("设备已接收任务，任务{},设备：{}",taskDataList.get(j).toString(),deviceDataList.get(i));
//                        taskDataList.get(j).setSid(String.valueOf(timeNow));
//                        String sid = SecureUtil.md5(taskDataList.get(j).getVideoName()+taskDataList.get(j).getRoomId()+timeNow+"sb1314520sbNB$");
//                        return AjaxResult.success(sid,taskDataList.get(j));
//                    }

//                }

                //如果任务列表中查不到任务 执行清空设备当前任务
                //删除任务列表中的任务 这里请求后会自动清除  也就是说在删除列表之前 要记录设备列表缓存数据
//                deviceDataList.get(i).setRoomId(null);

                deviceDataList.get(i).setStartWorkingState(null);

                deviceDataList.get(i).setDuration(0L);

                deviceDataList.get(i).setScreenImgUrl(null);

//                deviceDataList.get(i).setIp(null);
                log.info("设备清除任务数据，设备：{}",deviceDataList.get(i).toString());
                break;

            }
        }

        if (has == 0){
            //加入设备列表 初始化
            DeviceData deviceData = new DeviceData();
            deviceData.setDeviceId(deviceId);
            deviceData.setDeviceNickName(deviceNickName);
            deviceData.setCardNo(cardNo);
            deviceData.setPersonName(personName);
            deviceData.setState(System.currentTimeMillis());
            deviceData.setTodayTaskNumber(0);
            deviceData.setTodayTaskIntegral(0L);
            deviceData.setDuration(0L);
            deviceData.setIp(httpServletRequest.getRemoteAddr());
            deviceIndex =deviceDataList.size();
            deviceDataList.add(deviceData);
        }





        // 任务RoomId    设备列表RoomId roomid == “0”         脚本发送的RoomId

        //  1脚本初始化无roomId
        //  2脚本执行完任务    带roomid请求 任务列表中无roomid
        //  3脚本执行出错 重新领取任务 带上上一次的roomid  若无任务 设置设备列表roomid 为0
        //  4脚本离线 服务器回收roomId 设备列表roomId为0  //已回收任务
        //  5服务器分配任务 设备列表roomid为执行需要执行roomId

        //遍历任务
        Integer taskIndex = null;

        if (StrUtil.isEmptyIfStr(id) || deviceDataList.get(deviceIndex).getId() == null ){
            for (TaskData data : taskDataList){
                log.info("遍历任务列表分配任务");
                if (data.number>0 ){
                    data.number = data.number - 1;
                    taskData = data;
                    break;
                }
            }
        }
        else {
            for (int i = 0; i < taskDataList.size(); i++) {
                if (taskDataList.get(i).getId().equals(id)){
                    taskIndex = i;
                    break;
                }
            }
            if (taskIndex == null){
                // 脚本发送的roomid不在当前任务列表
                for (TaskData data : taskDataList){
                    log.info("遍历任务列表分配任务");
                    if (data.getNumber()>0 ){
                        data.setNumber(data.getNumber()-1);
                        taskData = data;
                        break;
                    }
                }
            }
            else if (!StrUtil.isEmptyIfStr(deviceDataList.get(deviceIndex).getId()) && deviceDataList.get(deviceIndex).getId().equals("0")){ //当roomId 为0的时候  1 设备离线 2 一直未进任务
                for (int i = 0; i < taskDataList.size(); i++) {
                    if (!taskDataList.get(i).getId().equals(id) && taskDataList.get(i).getNumber()>0){
                        taskDataList.get(i).setNumber(taskDataList.get(i).getNumber() - 1);
                        taskData = taskDataList.get(i);
                        break;
                    }
                }
            }
            else if(taskDataList.get(taskIndex).getId().equals(deviceDataList.get(deviceIndex).getId()) && !StrUtil.isEmptyIfStr(deviceDataList.get(deviceIndex).getId()) && !deviceDataList.get(deviceIndex).getId().equals("0") && !deviceDataList.get(deviceIndex).getId().equals(id) ){//脚本被分配任务

                taskData = taskDataList.get(taskIndex);

            }
            else {
                //上一次的id还在任务列表 说明

                //脚本执行出错 重新领取任务 带上上一次的roomId

                //回收脚本之前接的任务
                if (!StrUtil.isEmptyIfStr(deviceDataList.get(deviceIndex).getId()) && !deviceDataList.get(deviceIndex).getId().equals("0") && deviceDataList.get(deviceIndex).getId()!= null  &&deviceDataList.get(deviceIndex).getId().equals(id) ){
                    if (taskDataList.get(taskIndex).getNumber()+1<=taskDataList.get(taskIndex).getNumberStatic()){
                        taskDataList.get(taskIndex).setNumber(taskDataList.get(taskIndex).getNumber()+1);
                    }

                    deviceDataList.get(deviceIndex).setRoomId("0");
                    deviceDataList.get(deviceIndex).setId("0");
                    //取其他任务
                    for (int i = 0; i < taskDataList.size(); i++) {
                        if (!taskDataList.get(i).getId().equals(id) && taskDataList.get(i).getNumber()>0){
                            taskData = taskDataList.get(i);
                            taskDataList.get(i).setNumber(taskDataList.get(i).getNumber()-1);
                            break;
                        }
                    }
                }


//            for (TaskData data : taskDataList){
//                log.info("遍历任务列表分配任务");
//
//                //脚本执行出错 重新领取任务 带上上一次的roomid
//                //回收脚本之前接的任务
//
//
//
//
//
//
//                //脚本之前接过任务 再分配任务
////                if (data.number > 0 && !data.getRoomId().equals(roomId)) {
////                    data.number = data.number - 1;
////                    taskData = data;
////                    break;
////                }
//            }
        }
        }

        //根据taskData 是否为null 来确认分配到任务
        if (taskData!=null){

            log.info("成功分配到任务");

            deviceDataList.get(deviceIndex).setRoomId(taskData.roomId);

            deviceDataList.get(deviceIndex).setTodayTaskNumber(deviceDataList.get(deviceIndex).getTodayTaskNumber()+1);

            deviceDataList.get(deviceIndex).setHaveWorkTime(timeNow);

            deviceDataList.get(deviceIndex).setId(taskData.getId());

            taskData.setSid(String.valueOf(timeNow));

            String sid = SecureUtil.md5(taskData.getVideoName()+taskData.getRoomId()+timeNow+"sb1314520sbNB$");

            return AjaxResult.success(sid,taskData);
        }

       return AjaxResult.fail("暂无任务","");

    }

    @Auth(user = "1000")
    @PostMapping("/setTask")
    public AjaxResult setTask(@RequestBody TaskData taskData ){
        //参数校验
        if ( taskData.number ==null || taskData.number.equals(0)||taskData.number<0){
            return AjaxResult.fail(404,"设备数出错");
        }

        taskData.setCreatIntegral(0L);

        taskData.setNumberStatic(taskData.getNumber());

//        if (taskData.time ==null || taskData.time.isEmpty()){
//            return AjaxResult.fail(404,"请输入时间");
//        }

//        if (taskData.duration ==null || taskData.duration.isEmpty()){
//            return AjaxResult.fail(404,"请输入时长");
//        }

       Long duration= DateUtil.between( DateUtil.parse(taskData.beginTimeFrom),DateUtil.parse(taskData.beginTimeTo), DateUnit.MINUTE);
        if (duration<10){
            return AjaxResult.fail(404,"任务小于十分钟");
        }

        if (DateUtil.compare( DateUtil.parse(taskData.beginTimeTo),DateUtil.date())<1){

            return AjaxResult.fail(404,"任务最终时间必须大于当前时间");

        }
        if ( DateUtil.between( DateUtil.date(),DateUtil.parse(taskData.beginTimeTo), DateUnit.MINUTE)<10){
            return AjaxResult.fail(404,"任务时间小于十分钟");
        }

        taskData.setDuration(duration.toString());


        //设置截止时间戳
        taskData.setTime(String.valueOf(DateUtil.parse(taskData.beginTimeTo).getTime()));


        if (taskData.integral ==null || taskData.integral <= 0){
            return AjaxResult.fail(404,"请输入任务每分钟积分");
        }
        if (DateUtil.compare( DateUtil.parse(taskData.beginTimeFrom),DateUtil.date())>0){
            // 加入临时任务表
            log.info("addTempTask {}",taskData);
            if ( taskMapper.addTempTask(taskData)){
                return  AjaxResult.success();
            }
            else {
                return AjaxResult.fail(-1,"加入任务出错");
            }
        }

        if (taskData.getRoomAddress() !=null && !taskData.getRoomAddress().isEmpty()){
            //解析直播间roomId
            String roomId = xiguaAddress.getRoomId(taskData.roomAddress);
            if (roomId.equals("false")){
                return AjaxResult.fail(404,"地址解析错误");
            }
            //获取直播人名
            String videoName = xiguaAddress.getVideoName(taskData.roomAddress);
            if (videoName.equals("false")){
                return AjaxResult.fail(404,"直播人地址解析错误");
            }
            taskData.setRoomId(roomId);
            taskData.setVideoName(videoName);
        }
        if (taskData.getPersonAddress() !=null && !taskData.getPersonAddress().isEmpty()){
            //解析直播间roomId
            String roomId = xiguaAddress.getRoomIdByPersonAddress(taskData.getPersonAddress());
            if (roomId == null){
                return AjaxResult.fail(404,"地址解析错误");
            }
            //获取直播人名
            String videoName = xiguaAddress.getNickNameByPersonAddress(taskData.getPersonAddress());
            if (videoName == null){
                return AjaxResult.fail(404,"直播人地址解析错误");
            }

            String xiguaName = xiguaAddress.getXiGuaName(roomId);
            if (xiguaName == null){
                xiguaName = xiguaAddress.getXiGuaName(roomId);
            }
            taskData.setRoomId(roomId);
            taskData.setVideoName(videoName);
            taskData.setVideoNameXiGua(xiguaName);
        }

        if (StrUtil.isEmptyIfStr(taskData.getRoomId()) || StrUtil.isEmptyIfStr(taskData.getVideoName()) || !NumberUtil.isNumber(taskData.getRoomId())){
            return AjaxResult.fail(404,"RoomId 出错");
        }

        taskData.setBeginTimeFrom(DateUtil.date(Calendar.getInstance()).toString());
        taskData.setId(IdUtil.randomUUID());
        taskModel.setTask(taskData);

         return  AjaxResult.success();
    }



    @Auth(user = "1000")
    @GetMapping("/getTaskList")
    public AjaxResult getTaskList(){

        Long time = System.currentTimeMillis();

        for (int i = 0; i < taskDataList.size(); i++) {
            taskDataList.get(i).setNumberWorking(0);
            for (int j = 0; j < deviceDataList.size(); j++) {
               if (taskDataList.get(i).getId().equals(deviceDataList.get(j).getId()) && deviceDataList.get(j).getState() != null && deviceDataList.get(j).getLastWorkingState() != null && time- deviceDataList.get(j).getState()<1000*130 && time - deviceDataList.get(j).getLastWorkingState() <1000*30  ){
                   taskDataList.get(i).setNumberWorking(taskDataList.get(i).getNumberWorking()+1);
               }
            }
        }

        //缓存表中任务
        List<TaskData> tempTaskDataList =  taskMapper.selectAllTempTask();

//        log.info("tempTaskDataList：{}",tempTaskDataList);

         tempTaskDataList.addAll(taskDataList);

        return  AjaxResult.success(tempTaskDataList);

    }

    @Auth(user = "1000")
    @GetMapping("/getTempTaskList")
    public AjaxResult getTempTaskList(@PathParam("page")Integer page ,@PathParam("size") Integer size ){

        if (page>0&&size>0){
            return  AjaxResult.success(taskMapper.getTempTaskListCount().toString(),taskMapper.getTempTaskList((page-1)*size,size));
        }

        return AjaxResult.fail(-1,"");

    }

    @Auth(user = "1000")
    @GetMapping("/getHistoryTaskList")
    public AjaxResult getHistoryTaskList(@PathParam("page")Integer page ,@PathParam("size") Integer size ){

        if (page>0&&size>0){

            return  AjaxResult.success(taskMapper.getTaskListCount().toString(),taskMapper.getTaskList((page-1)*size,size));

        }

       return AjaxResult.fail(-1,"");
    }

    @Auth(user = "1000")
    @PostMapping("/deleteHistoryTask")
    public AjaxResult deleteHistoryTask(@RequestBody List<TaskData> taskDataList ){

        return AjaxResult.success(taskMapper.deleteHistoryTasks(taskDataList));

    }


    /* 1校验
     * 2删除任务 清空用户缓存积分
     *  清除设备列表数据  列表数据在删除了任务之后脚本请求接收任务 发现当前执行任务被删除 自动清除数据，
     * 或在执行的任务心跳找不到对应任务，脚本结束任务 重新请求接收任务也会自动清除数据
     * */
    //删除任务

    @Auth(user = "1000")
    @PostMapping("/deleteTask")
    public AjaxResult deleteTask(@RequestBody TaskData taskData){
        //1校验
        if (taskData.getId()==null || taskData.getId().length() ==0){
           return AjaxResult.fail(-1,"请输入正确roomId");
        }
        if (isNumeric0(taskData.getId())){
            if (taskMapper.selectCountByIdTempTask(Integer.valueOf(taskData.getId())) == 1){
                taskMapper.deleteTempTask(taskData);
                return AjaxResult.success();
            }
        }
        else {
            for (int i=0; i<taskDataList.size();i++){
                if (taskData.getId().equals(taskDataList.get(i).getId())){
                    //删除任务总方法
                    Boolean  delete = taskModel.deleteTaskById(taskData.getId());
                    if (delete){
                        return AjaxResult.success();
                    }
                }
            }
        }


        return  AjaxResult.fail(404,"没有找到任务");
    }



    //脚本状态链接
    @PostMapping("/checkState")
    public AjaxResult checkState(@RequestBody CheckInfo checkInfo,@PathParam("mid") String mid){

        //只有在脚本接收任务后才会请求该接口 每十秒记录一次任务状态
        String md5 = SecureUtil.md5(checkInfo.getCardNo()+checkInfo.getDeviceId()+checkInfo.getRoomId()+checkInfo.getTime()+checkInfo.getVideoDieOut()+checkInfo.getTaskState()+checkInfo.getId()+"sb1314520sbNB$");
        if (!md5.equals(mid)){
            return  AjaxResult.fail(-1,"?????你在做什么,唱歌");
        }



//         log.info("checkState:{}",checkInfo.getVideoDieOut());
        if (!StrUtil.isEmptyIfStr(checkInfo.getVideoDieOut())&&checkInfo.getVideoDieOut().equals("true")){//脚本发现直播间任务结束
            log.info("checkState:{}",checkInfo.getVideoDieOut());
            for (int i = 0; i < taskDataList.size(); i++) {
                if (taskDataList.get(i).getId().equals(checkInfo.getId())){ //找到任务直播间 删除他
                    taskModel.deleteTaskById(checkInfo.getId());
                }
            }
        }


        log.info("sssssssssssssssss");
        //一般 参数 账号、设备唯一标识、设备昵称、直播间id、任务状态（在任务直播间或不在）、当前时间、md5校验

        Long systemTime = System.currentTimeMillis();

        //返回状态  0 停止任务 1 正常继续执行
        int state = 0;
        //顺带查找当前任务积分/每分钟
        AtomicInteger integral = new AtomicInteger(0);
        log.info("taskDataList:{}",taskDataList.size());
        //查询当前直播间列表 判断脚本发送的直播间任务是否有效
        for(int i=0;i<taskDataList.size();i++){
            log.info("taskDataList:{}",taskDataList.size());
            if (taskDataList.get(i).getId().equals(checkInfo.getId())){
                if (taskDataList.get(i).getIntegral()!=null&&taskDataList.get(i).getIntegral()>0 ){
                    integral.set(taskDataList.get(i).getIntegral());
                }
                  state =1;
            }
        }


        //积分统计
        // 任务有效 且脚本发送 在任务直播间中
        if (  state ==1 && checkInfo.getTaskState().equals("true") ){
             AtomicInteger isWork = new AtomicInteger(0);

             log.info("任务有效 且脚本发送 在任务直播间中");
                       log.info("开启多遍历线程");
                       deviceDataList.stream().parallel().forEachOrdered(item -> {
                           //找到设备位置  第一次发送请求 接受到workingTime
                           if (item.getDeviceId().equals(checkInfo.deviceId)&&item.getStartWorkingState() ==null){

                               log.info("第一次找到 device ");
                               item.setStartWorkingState(systemTime);
                               item.setLastWorkingState(systemTime);
                               item.setState(systemTime);
                               item.setDuration(Long.parseLong("0"));
                           }

                           else if (item.getDeviceId().equals(checkInfo.deviceId)){
                               //有效时间段请求
                               long l =  systemTime - item.getLastWorkingState();
                               log.info("第二次找到device {}", item);

                               if (l<1000*20 && l>1000*10) {
                                   log.info("脚本发送时间有效,记录当前时间和统计积分{}",item);
                                   item.setLastWorkingState(systemTime);
                                   item.setDuration(l+item.getDuration());
//                                 item.setTodayTaskIntegral(((long) (int) l*integral.get()/60000)+item.getTodayTaskIntegral()); //今日积分
                                   item.setTodayTaskIntegral(((long) integral.get()/6)+item.getTodayTaskIntegral()); //今日积分
                                   isWork.set((int)l);
                               }

                               else {
                                   item.setLastWorkingState(systemTime);
                                   log.info("脚本发送时间无效,记录下当前时间从次开始判断有效时间{}",item);
                               }
                               item.setState(systemTime);
                           }

                       });
                    //增加用户积分
                   if (integral.get()!=0&&isWork.get()!=0){
                       log.info("增加积分");
                       for (int i = 0; i <userListGlobal.size(); i++) {
                           log.info("增加积分111111111111");
                           if (userListGlobal.get(i).getCardNo().equals(checkInfo.getCardNo())){
                               log.info("增加积分2");
                               if (userListGlobal.get(i).getTempIntegral()==null ){ userListGlobal.get(i).setTempIntegral(0L); }
//                              userListGlobal.get(i).setTempIntegral((long)integral.get()*isWork.get()/60000+ userListGlobal.get(i).getTempIntegral());
                                userListGlobal.get(i).setTempIntegral((long)integral.get()/6 + userListGlobal.get(i).getTempIntegral());
//                                log.info("user success add integral :{}",userListGlobal.get(i));
                                break;
                           }
                       }

                       log.info("add task integral ");

                       for (int i = 0; i <taskDataList.size(); i++) {
                           log.info("add task  integral 1111{}",checkInfo.getCardNo());
                           if (taskDataList.get(i).getId().equals(checkInfo.getId())){
//
//                               log.info(taskDataList.get(i).getCreatIntegral().toString());

//                               taskDataList.get(i).setCreatIntegral((long)integral.get()*isWork.get()/60000+ taskDataList.get(i).getCreatIntegral());
                               taskDataList.get(i).setCreatIntegral((long)integral.get()/6+ taskDataList.get(i).getCreatIntegral());

                           }
                       }

//                    userListGlobal.stream().forEach( user -> {
//                           if (user.getCardNo().equals(checkInfo.getCardNo())){
//                               if (user.getTempIntegral()==null ){ user.setTempIntegral((long)0); }
//                               user.setTempIntegral((long)integral.get()*isWork.get()/60000+ user.getTempIntegral());
//                               log.info("用户：{}增加积分成功",user);
//                           }
//                    });
                   }

                   return AjaxResult.success();
           }
        // 任务有效 在任务中 但是还没进入直播间 //刷新设备在线状态
        else if (state == 1){
            log.info("任务有效 在任务中 但是还没进入直播间 //刷新设备在线状态state:{}",state);
            deviceDataList.stream().parallel().forEach(item ->{
                if (item.getDeviceId().equals(checkInfo.deviceId)){
                    item.setState(systemTime);
                    log.info("刷新设备在线状态{}",item);
                }
            });
                   return  AjaxResult.success();
           }

         log.info("state:{}",state);
        return AjaxResult.fail(400,"任务已失效");
        }



    /*
     *
     * jiao ben jie tu 上传接口
     *
     * */
    @Value("${server.port}")
    String  port;
    @PostMapping("/screenUpload")
    public AjaxResult qrUpload(@RequestParam(value = "file") MultipartFile multipartFile,@PathParam("roomId") String roomId,
                               @PathParam("videoName") String videoName ,@PathParam("deviceId") String deviceId,
                               @PathParam("time") String time ,@PathParam("mid1") String mid1 ,@PathParam("mid2") String mid2) throws IOException {

//        上传文件 校验  ？？

        String md5 = SecureUtil.md5(roomId+videoName+deviceId+mid1+time+"sb1314520sbNB$");

        log.info(multipartFile.toString());

        log.info("md5:{},mid1:{},mid2:{},deviceId:{}",md5,mid1,mid2,deviceId);

        if (!md5.equals(mid2)){
            return  AjaxResult.fail(-1,"?????你在做什么,唱歌");
        }
        if (!getMd5(multipartFile).equals(mid1)){
            return  AjaxResult.fail(-1,"?????文件改了");
        }

          int state = 0;
        for (int i = 0; i < taskDataList.size(); i++) {
            if (taskDataList.get(i).getRoomId().equals(roomId) && taskDataList.get(i).getVideoName().equals(videoName)){
                state=1;
            }
        }
        if (state==0){return  AjaxResult.fail(-1,"1");}
           state = 0;

        for (int i = 0; i < deviceDataList.size(); i++) {
            log.info("deviceIdL:{},deviceId:{}",deviceDataList.get(i).getDeviceId(),deviceId);
            if (deviceDataList.get(i).getDeviceId().equals(deviceId)){
                state = 1;
            }
        }

        if (state==0){return  AjaxResult.fail(-1,"2");}

        try {
            String name = file.adddeviceScreenImg(multipartFile,roomId,videoName,deviceId);
            if (name == null || name.length() == 0) {
                return AjaxResult.fail(404, "上传文件错误");
            }
            for (int i = 0; i < deviceDataList.size() ; i++) {
                if (deviceDataList.get(i).getDeviceId().equals(deviceId)){
                    deviceDataList.get(i).setScreenImgUrl(name);
                }
            }

            // this can set a  function

            log.info(name);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return AjaxResult.fail(-1, "出错");

    }



    public String getMd5(MultipartFile file) {
        try {
            byte[] uploadBytes = file.getBytes();
            //file->byte[],生成md5
            String md5Hex = DigestUtils.md5Hex(uploadBytes);
            //file->InputStream,生成md5
            String md5Hex1 = DigestUtils.md5Hex(file.getInputStream());
            //对字符串生成md5
            String s = DigestUtils.md5Hex("字符串");
            return md5Hex ;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static boolean isNumeric0(String str) {

        for(int i=str.length();--i>=0;)
        {
        int chr=str.charAt(i);
        if(chr<48 || chr>57)
            return false;
        }
        return true;
    }

}




