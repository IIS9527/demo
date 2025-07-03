//package com.example.demo;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.net.url.UrlBuilder;
//import cn.hutool.core.util.CharsetUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.example.demo.Address.XiguaAddress;
//import com.example.demo.Controller.AdminController;
//import com.example.demo.Data.DeviceData;
//import com.example.demo.Data.GlobalVariablesSingleton;
//import com.example.demo.Data.TaskData;
//import com.example.demo.Data.User;
//import com.gargoylesoftware.htmlunit.BrowserVersion;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import lombok.Synchronized;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
//@Slf4j
//@SpringBootTest
//public class test {
//
//    @Autowired
//    XiguaAddress xiguaAddress;
//    @Autowired
//    AdminController adminController;
//    @Test
//    void  test1(){
//
//
//        List<DeviceData> deviceDataList = new ArrayList<>(31151581);
//
//
//        for (int i = 0; i < 3115; i++) {
//            DeviceData deviceData =  new DeviceData();
//            deviceData.setCardNo("33"+i);
//            deviceDataList.add(deviceData);
//        }
//
//        long startTime = System.nanoTime();
//        for (int j= 0; j < 1000; j++) {
//        deviceDataList.stream().parallel().forEach(deviceData -> {
//
//            if (   deviceData.getCardNo().equals("3310")){
////                log.error(String.valueOf(1));
//                deviceData.setTodayTaskIntegral(10L);
//            }
//
//        });
//}
////        for (int j= 0; j < 1000; j++) {
////            for (int i = 0; i < deviceDataList.size(); i++) {
////
////                if (   deviceDataList.get(i).getCardNo().equals("3310")){
//////                    log.error(String.valueOf(i));
//////                    log.error(String.valueOf(i));
//////                    log.error(String.valueOf(i));
////                    deviceDataList.get(i).setTodayTaskIntegral(10L);
////                    break;
////                }
////            }
////        }
//        long duration = System.nanoTime() - startTime;
//
//        System.out.println(  duration  + " ms");
//
//
//        startTime = System.nanoTime();
//
//        duration = System.nanoTime() - startTime;
//        System.out.println( duration  + " ms");
//
//
//
//
//
//    }
//    @Test
//    public  void  test2(){
//        Integer l = 0;
//        try {
//            ArrayList<DeviceData> list = new ArrayList<>();
//            int size = Integer.MAX_VALUE - 8;  // A bit less than Integer.MAX_VALUE to avoid OOM
//            System.out.println("Attempting to create an ArrayList with size: " + size);
//
//            for (int i = 0; i < size; i++) {
//                DeviceData deviceData =  new DeviceData();
//                deviceData.setCardNo("33"+i);
//                l=i;
//                list.add(deviceData);
//            }
//
//            System.out.println("Successfully created an ArrayList with size: " + list.size());
//        } catch (OutOfMemoryError e) {
//
//            System.out.println("Failed to create the ArrayList due to insufficient memory.");
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            System.out.println("Failed to create the ArrayList due to insufficient memory."+l);
//        }
//    }
//
//}
