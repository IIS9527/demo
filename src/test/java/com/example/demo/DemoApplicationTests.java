package com.example.demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.example.demo.Address.XiguaAddress;
import com.example.demo.Controller.AdminController;
import com.example.demo.Controller.TaskController;
import com.example.demo.Data.*;
import com.example.demo.Mapper.IntegralMapper;
import com.example.demo.Mapper.TaskMapper;
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
import java.util.Date;
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
@Autowired
    TaskMapper taskMapper;

@Test
    void test4() {

//    Date currentDate = new Date(); // current date/time
//    Date date15DaysAgo = DateUtil.offsetDay(currentDate, -2);
//    String formattedDateTime = DateUtil.format(date15DaysAgo, "yyyy-MM-dd HH:mm:ss");
//    log.error(formattedDateTime);
//    integralMapper.deleteExchangeIntegralbyAllowTime(formattedDateTime);
//
//    taskMapper.deleteTaskByTime(formattedDateTime);
//
}

}
