package com.example.demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.example.demo.Address.XiguaAddress;
import com.example.demo.Controller.AdminController;
import com.example.demo.Controller.TaskController;
import com.example.demo.Data.*;
import com.example.demo.Mapper.IntegralMapper;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Model.TaskModel;
import com.example.demo.common.DynamicScheduleTask;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.HttpClientUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@SpringBootTest
class DemoApplicationTests {
@Autowired
    XiguaAddress xiguaAddress;
@Autowired
    TaskController taskController;
@Autowired
GlobalVariablesSingleton globalVariablesSingleton;
@Autowired
    TaskModel taskModel;
@Autowired
    IntegralMapper integralMapper;
@Autowired
    AdminController adminController;
@Autowired
    UserMapper userMapper;
@Autowired
    DynamicScheduleTask dynamicScheduleTask;

@Test
    void test4() {

    xiguaAddress.getRoomId("https://v.douyin.com/iYKGpvAT/");



//    https://www.iesdouyin.com/web/api/v2/user/info/?sec_uid=MS4wLjABAAAATpNX5KX6UVnvIa1laS-QvhEIcugximbVYvPXtbuaqBGRHTcsgMOuLpjBWKDwGUSC&from_ssr=1

//       HtmlPage htmlPage= xiguaAddress.getTimeByHtmlUnit("https://v.douyin.com/iR3oHXAc/");
//       log.info(xiguaAddress.getRoomId("https://v.douyin.com/iR3oHXAc/"));
//String address = "https://v.douyin.com/iR3oEKwG/" ;
//       address = "https://live.douyin.com/345990528044";
//
//    Long systemTime = System.currentTimeMillis();
//
//   Long  s=0L;
//   Long  l = 0L;
//    for (int i = 0; i < 10000; i++) {
//
//       s =  (RandomUtil.getRandom().nextLong(10000,10500) )* 100 /60000 + s;
//       l = l+60/6;
//    }
//
//    System.out.println(s);
//    System.out.println(l);
//    System.out.println(60/6);












       try  {
//        HtmlPage page =xiguaAddress.getTimeByHtmlUnit(address);
//            List<HtmlElement> p =  page.getBody().getElementsByAttribute("a","class","B3AsdZT9");
//            String url = null;
//            for (int i = 0; i < p.size(); i++) {
//                String     reg = "(?<=room_id=).*?(?=&)";
//                Pattern  pattern = Pattern.compile(reg);
//                Matcher  matcher = pattern.matcher(p.get(i).toString());
//                if (matcher.find()){
//                    log.info(p.get(i).toString());
//                    reg = "(?<=href=\").*?(?=\")";
//                    pattern = Pattern.compile(reg);
//                    matcher = pattern.matcher(p.get(i).toString());
//                    if (matcher.find()){
//                        log.info("1{}",matcher.group());
//                        url = matcher.group();
//                    }else {
//                        log.info("ssssssss");
//                    }
//
//                }
//
//
//
//            }

//
//            HtmlPage page =xiguaAddress.getTimeByHtmlUnit(address);
//            HtmlElement  p = page.getBody();
//            String roomid = page.getElementsByTagName("script").toString();
////            log.info(roomid);
//           String     reg = "(?<=roomId\":\").*?(?=\",\"web_rid)";
//           Pattern  pattern = Pattern.compile(reg);
//           Matcher  matcher = pattern.matcher(roomid);
//           matcher.find();
//           log.info("11111111111{}",matcher.group());


//          xiguaAddress.getVideoName("https://v.douyin.com/iR3oHXAc/");



//        xiguaAddress.getRoomId("https://v.douyin.com/iR3oEKwG/");
//        HtmlPage page =xiguaAddress.getTimeByHtmlUnit(address);
//
//        log.info(page.getBody().getElementsByAttribute("a","class","B3AsdZT9").toString());
//
//        List<HtmlElement> pages = page.getHtmlElementById("root").getElementsByAttribute("div","class","RPhIHafP");
//
//
//        String reg = "(?<=reflow/).*?(?=\\?u_code)";
//        String temp = "aAabcBc";
//        Pattern pattern = Pattern.compile(reg);
//        log.info(page.getBaseURI().toString());
//        Matcher matcher = pattern.matcher(page.getBaseURI());
//        if( matcher.find() ){
//            System.out.println(matcher.group());
//
//        }

//            UrlBuilder builder = UrlBuilder.ofHttp(page.getBaseURI(), CharsetUtil.CHARSET_UTF_8);
//
//            log.info(builder.getQuery().toString());
//
//            return builder.getQuery().get("room_id").toString();


    }
    catch ( Exception e){

    }
}

//
//HttpServletRequest httpServletRequest = new MockHttpServletRequest();
//    TaskData taskData = new TaskData();
//        taskData.setRoomAddress("https://v.douyin.com/idQfoPT2/");
//        taskData.setTime(String.valueOf(System.currentTimeMillis()+999999l));
//        taskData.setDuration("999");
//        taskData.setNumber(999);
//        taskData.setRoomId("7286800239057865532");
//        taskData.setVideoName("漫游太空");
//        taskData.setIntegral(100);

//        globalVariablesSingleton.getTaskDataArrayList().add(taskData);

//          log.info(taskController.setTask(taskData).toString());
//
//
//
//
//
//            final long timeInterval = 10;
//        Runnable runnable = () -> {
//            int i=0;
//           while (i<3){
//                for (int j=0;j<100;j++){
//                     String cardNo = "1234";
//                     String deviceId = i+""+j;
//                     String deviceNickName = i+""+j;
//                     String personName= i+""+j;
//                     String time = i+""+j;
//                    log.info(taskController.getTask(cardNo,personName,time,deviceId,deviceNickName,"1",httpServletRequest).toString());
//
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//            }
//                i++;}
//        };
//
//        Thread thread = new Thread(runnable);
//        thread.start();
//
//while (true){
//
//    log.info(globalVariablesSingleton.getTaskDataArrayList().toString());
//    dynamicScheduleTask.pushMessage();
//    log.info(globalVariablesSingleton.getTaskDataArrayList().toString());
//    try {
//        Thread.sleep(5000);
//    } catch (InterruptedException e) {
//        e.printStackTrace();
//    }
//
//}


//}










//    }




//@Test
//void test3(){
//    JSONObject jsonObject =new JSONObject();
//    jsonObject.set("cardNo","1234");
//    jsonObject.set("password","123");
//
//   log.info( adminController.creatAccount(jsonObject).toString());
//
//
//}
//
//
//
//
//@Test
//void  test2(){
//    ExchangeIntegral exchangeIntegral = new ExchangeIntegral();
//    exchangeIntegral.setExchangeTime(DateUtil.date().toString());
//    exchangeIntegral.setRealName("张三");
//    exchangeIntegral.setCardNo("112349");
//    exchangeIntegral.setAllowState(0);
//    exchangeIntegral.setPage(0);
//    exchangeIntegral.setSize(100);
//    exchangeIntegral.setIntegral(100l);
//    exchangeIntegral.setQrUrl("123324");
////    exchangeIntegral.setAllowTime(DateUtil.date().toString());
//
//
//    integralMapper.addExchangeIntegral(exchangeIntegral);
//    exchangeIntegral.setAllowState(null);
//    exchangeIntegral.setPage(1);
//    exchangeIntegral.setSize(5);
//    exchangeIntegral.setIntegral(null);
//    exchangeIntegral.setQrUrl(null);
//    exchangeIntegral.setExchangeTime(null);
//    integralMapper.selectExchangeIntegralList(exchangeIntegral);
//
//
//}
//
//
//
//@Test
//void  test(){
//    User user = new User();
//    user.setCardNo("aa");
//    user.setTempIntegral( (long)10000);
//    User user2 = new User();
//    user2.setCardNo("ss");
//    GlobalVariablesSingleton.getInstance().getUsers().add(user);
//    GlobalVariablesSingleton.getInstance().getUsers().add(user2);
//    taskModel.updateUser();
//
//
//}
//
//
//    @Test
//    void contextLoads() {
//
//        TaskData taskData = new TaskData();
//        taskData.setRoomAddress("https://v.douyin.com/idF7pmHv/");
//        taskData.setTime("999999999");
//        taskData.setDuration("999");
//        taskData.setNumber(999);
//        taskData.setRoomId("7286800239057865532");
//        taskData.setVideoName("漫游太空");
//        taskData.setIntegral(100);
////        taskController.setTask(  taskData);
//        GlobalVariablesSingleton.getInstance().getTaskDataArrayList().add(taskData);
//        log.info(   GlobalVariablesSingleton.getInstance().getTaskDataArrayList().toString());
//        for (int i=0;i<10000;i++){
//            String j=i+"ss";
//            taskController.getTask("ss",j,j,j,j,j);
//        }
//        for (int i=0;i<10000;i++){
//            String j=i+"aa";
//            taskController.getTask("aa",j,j,j,j,j);
//        }
//
//        log.info("globDeviceSize:{}",GlobalVariablesSingleton.getInstance().getDeviceDataArrayList().size());
//
//        User user = new User();
//        user.setCardNo("aa");
//        user.setTempIntegral( (long)10000);
//        User user2 = new User();
//        user2.setCardNo("ss");
//        user2.setTempIntegral(10000l);
//        GlobalVariablesSingleton.getInstance().getUsers().add(user);
//        GlobalVariablesSingleton.getInstance().getUsers().add(user2);
//
//
//
//        final long timeInterval = 100;
//        Runnable runnable = () -> {
//            int i=0;
//           while (i<3){
//                for (int j=0;j<100;j++){
//                    CheckInfo checkInfo =new CheckInfo();
//                    checkInfo.setCardNo("ss");
//                    checkInfo.setDeviceId(j+"ss");
//                    checkInfo.setTime(System.currentTimeMillis());
////                    checkInfo.setTaskState(j%2== 1? "true":"false");
//                    checkInfo.setTaskState("true");
//                    checkInfo.setRoomId("7286800239057865532");
//                  System.out.println(  taskController.checkState(checkInfo,"s").toString());
//                    try {
//                        Thread.sleep(timeInterval);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//            }
//                i++;}
//        };
//        Thread thread = new Thread(runnable);
//        thread.start();
//        Runnable runnable2 = new Runnable() {
//            @Override
//            public void run() {
//                int i =0;
//                  while (i<10){
//                    for (int j=0;j<100;j++){
//                        CheckInfo checkInfo =new CheckInfo();
//                        checkInfo.setCardNo("aa");
//                        checkInfo.setDeviceId(j+"aa");
//                        checkInfo.setTime(System.currentTimeMillis());
////                        checkInfo.setTaskState(j%2== 1? "true":"false");
//                        checkInfo.setTaskState("true");
//                        checkInfo.setRoomId("7286800239057865532");
//                        taskController.checkState(checkInfo,"s");
//                        try {
//                            Thread.sleep(timeInterval);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    log.info("userList:{}",GlobalVariablesSingleton.getInstance().getUsers());
//                i++; }
//                }
//
//        };
//
//        Thread thread2 = new Thread(runnable2);
//        thread2.start();
//
//
//
//while (true){
//
//    try {
//
//
//        Thread.sleep(20000);
//      log.info("getUserList:{}",adminController.getUserList(user).toString());
//
//    } catch (InterruptedException e) {
//        e.printStackTrace();
//    }
//
//}
//
//
//
//
//   }
//
}
