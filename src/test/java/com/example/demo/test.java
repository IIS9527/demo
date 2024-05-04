package com.example.demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.Address.XiguaAddress;
import com.example.demo.Data.GlobalVariablesSingleton;
import com.example.demo.Data.User;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
@Slf4j
@SpringBootTest
public class test {

    @Autowired
    XiguaAddress xiguaAddress;
    @Test
    void  test1(){

        System.out.println(DateUtil.date());


//        System.out.println( xiguaAddress.getRoomIdByPersonAddress("https://v.douyin.com/i2jDxEfu/"));
//
         System.out.println( xiguaAddress.getXiGuaName("7363084478077586213"));



    }

}
