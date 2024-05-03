package com.example.demo.Controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.example.demo.Config.AjaxResult;
import com.example.demo.Config.File;
import com.example.demo.Data.*;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Model.DevicesModel;
import com.example.demo.Model.IntegralModel;
import com.example.demo.Config.ApplicationVariable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
@Slf4j
@RestController
@RequestMapping("/user")
public class UserQueryController {

    List<DeviceData> deviceDataList = GlobalVariablesSingleton.getInstance().getDeviceDataArrayList();

    List<User> userList =  GlobalVariablesSingleton.getInstance().getUsers();

    @Autowired
    UserMapper userMapper;

    @Autowired
    IntegralModel integralModel;

    @Autowired
    File file;

    @Autowired
    DevicesModel devicesModel;

    @GetMapping("/deviceDataList")
    public AjaxResult getDeviceDataList(@PathParam("page") Integer page, @PathParam("size")Integer size,   @PathParam("state") String state, HttpServletRequest request){


        HttpSession session = request.getSession();

        User user= (User)session.getAttribute(ApplicationVariable.SESSION_KEY_USERINFO);
        //参数校验
        if (user.getCardNo() == null) return AjaxResult.fail(404,"怎么没账号");

//         log.info("device{},state:{}",deviceDataList,state);

        List<DeviceData> userDeviceDataList = devicesModel.findDevices(user.getCardNo(), state);

        return  AjaxResult.success(String.valueOf(userDeviceDataList.size()),userDeviceDataList);

    }
/*
*
* 查询个人资料接口
*
* */
    @GetMapping("/myInfo")
    public AjaxResult getMyInfo(HttpServletRequest request){
        User user  = (User)request.getSession().getAttribute(ApplicationVariable.SESSION_KEY_USERINFO);

        if (user == null) return AjaxResult.fail(404,"");

        User  myUserInfo =  userMapper.selectMyInfoByCardNo(user.getCardNo());

        if (myUserInfo==null) return AjaxResult.fail(404,"没有你的资料哦");

        userList.stream().parallel().forEach( user1 -> {
            if (user1.getCardNo().equals(myUserInfo.getCardNo())){
                log.info("yourInfo");
               myUserInfo.setTempIntegral( myUserInfo.getTempIntegral()+user1.getTempIntegral());
            }

        });

        return AjaxResult.success(myUserInfo);

    }
    /*
     *
     * 设置个人资料接口
     *
     * */
    @PostMapping("/setMyInfo")
    public AjaxResult setMyInfo(@RequestBody User user, HttpServletRequest request){

        if (StrUtil.isEmptyIfStr(user.getPassword()) && StrUtil.isEmptyIfStr(user.getRealName())){
            return AjaxResult.fail(-1,"请输入参数");
        }

        User localUser  = (User)request.getSession().getAttribute(ApplicationVariable.SESSION_KEY_USERINFO);

        //替换session账号防止注入 cardNo

         user.setCardNo(localUser.getCardNo());

         if (userMapper.setMyInfo(user)){
             return AjaxResult.success();
         }

        return AjaxResult.fail(-1,"更新失败");
    }
    /*
     *
     * 申请积分接口
     *
     * */
    @PostMapping("/applyExchange")
    public AjaxResult  applyExange(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        User localUser  = (User)request.getSession().getAttribute(ApplicationVariable.SESSION_KEY_USERINFO);

        Long integral = jsonObject.getLong("integral");


        if ( integral==null || integral < 100  ){
            return  AjaxResult.fail(-1,"兑换积分过小");
        }
        localUser = userMapper.selectMyInfoByCardNo(localUser.getCardNo());

        if (localUser.getAvailableIntegral() < integral ){
            return  AjaxResult.fail(-1,"可用积分不足");
        }
        if (localUser.getRealName() == null || localUser.getRealName().length() ==0 ){
            return  AjaxResult.fail(-1,"请先设置真实姓名");
        }
        if (localUser.getQrUrl() == null || localUser.getQrUrl().length() ==0 ){
            return  AjaxResult.fail(-1,"请先设置二维码");
        }

        //进入用户积分申请
        if (integralModel.applyExchange(localUser,integral)){
          return  AjaxResult.success("申请成功");
        }


        return AjaxResult.fail(-1,"申请失败");
    }
    /*
     *
     * 查询个人积分申请接口
     *
     * */
    @GetMapping("/MyApplyExchangeIntegral")
    public AjaxResult getMyApplyExchangeIntegral(@Param("page") Integer page,@Param("size") Integer size,@PathParam("searchData")String searchData, HttpServletRequest request){
        User user = (User)request.getSession().getAttribute(ApplicationVariable.SESSION_KEY_USERINFO);

       List<ExchangeIntegral> exchangeIntegrals = userMapper.selectMyExchangeIntegral(user.getCardNo());

       return  AjaxResult.success(exchangeIntegrals);

    }
    /*
     *
     * 二维码上传接口
     *
     * */
    @Value("${server.port}")
    String  port;
    @PostMapping("/qrUpload")
    public AjaxResult qrUpload(@RequestParam(value = "file") MultipartFile multipartFile, HttpServletRequest request) throws IOException {

        User user = (User) request.getSession().getAttribute(ApplicationVariable.SESSION_KEY_USERINFO);
        //上传文件 校验  ？？

        try {

            String name = file.addUserQRImg(multipartFile);
            if (name == null || name.length() == 0) {
                return AjaxResult.fail(404, "上传文件错误");
            }

            user.setQrUrl( name);
            userMapper.setMyInfo(user);
            log.info(name);
            return AjaxResult.success(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return AjaxResult.fail(-1, "出错");

    }










}
