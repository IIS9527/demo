package com.example.demo;

import cn.hutool.core.date.DateUtil;
import com.example.demo.Data.GlobalVariablesSingleton;
import com.example.demo.Data.User;
import lombok.Synchronized;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class test {
    @Test
    void  test1(){
//        User user = new User();
        System.out.println(DateUtil.date());
//
//        GlobalVariablesSingleton.getInstance().getUsers().add(user);
//        GlobalVariablesSingleton.getInstance().getUsers().get(0).setTempIntegral(0L);
//   AtomicInteger atomicInteger =new AtomicInteger();
//   atomicInteger.set(0);
//        Runnable runnable2 = new Runnable() {
//
//            @Override
//            public synchronized void run() {
//                 int i =0;
//
//                while (i<10000000){
//                    i++;
//                    atomicInteger.set(atomicInteger.get()+1);
//                    GlobalVariablesSingleton.getInstance().getUsers().get(0).setTempIntegral(GlobalVariablesSingleton.getInstance().getUsers().get(0).getTempIntegral()+1);
//                }
//                System.out.println("s"+atomicInteger.get()+"S"+ GlobalVariablesSingleton.getInstance().getUsers().get(0).getTempIntegral());
//            }
//
//        };
//        Thread thread2 = new Thread(runnable2);
//        thread2.start();
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                int  i =0;
//                    while (i<20){
//                        i++;
//                        atomicInteger.set(atomicInteger.get()-1);
//                        GlobalVariablesSingleton.getInstance().getUsers().get(0).setTempIntegral(GlobalVariablesSingleton.getInstance().getUsers().get(0).getTempIntegral()-1);
//
//                    }
//
//                System.out.println(atomicInteger.get()+"ss"+ GlobalVariablesSingleton.getInstance().getUsers().get(0).getTempIntegral());
//            }
//
//        };
//        Thread thread = new Thread(runnable);
//        thread.start();
//        while (true){
//            try {
//                System.out.println(atomicInteger.get()+"ss"+GlobalVariablesSingleton.getInstance().getUsers().get(0).getTempIntegral());
//                Thread.sleep(10000);
//                GlobalVariablesSingleton.getInstance().getUsers().get(0).getTempIntegral();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//
    }

}
