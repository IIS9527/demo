package com.example.demo;

import cn.hutool.core.date.DateUtil;
import com.example.demo.Address.XiguaAddress;
import com.example.demo.Data.GlobalVariablesSingleton;
import com.example.demo.Data.User;
import lombok.Synchronized;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class test {

    @Autowired
    XiguaAddress xiguaAddress;
    @Test
    void  test1(){

        System.out.println(DateUtil.date());


        System.out.println( xiguaAddress.getRoomIdByPersonAddress("https://v.douyin.com/i2jDxEfu/"));

        System.out.println( xiguaAddress.getVideoName("https://v.douyin.com/iYKGpvAT/"));

    }

}
