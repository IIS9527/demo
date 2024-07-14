package com.example.demo;


import com.example.demo.Address.XiguaAddress;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class test2 {


    @Autowired
    private XiguaAddress xiguaAddress;

    @Test
    public void test(){
        
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(6000);

        // -----重点-----设置为我们自定义的错误处理类

//        webClient.setJavaScriptErrorListener(new XiguaAddress.MyJSErrorListener());

        webClient.setJavaScriptTimeout(5000);

        try {
            HtmlPage page =webClient.getPage("http://127.0.0.1:9527/outback/list?page=1&limit=25&sort=add_time&order=desc&start=&end=");
            System.out.println(page);
        } catch (IOException e) {
            e.printStackTrace();
        }

        webClient.waitForBackgroundJavaScript(6000);

//        List<Integer> list =new ArrayList<>();
//
//        for (int i = 0; i < 10000; i++) {
//            list.add(i);
//        }
//
//        Runnable runnable2 = new Runnable(){
//            @Override
//            public void run() {
//                while (true){
//
//
//                    for (int j = 0; j < list.size(); j++) {
//                        list.get(j);
//                    }
//                }
//            }
//        };
//
//        Runnable runnable = new Runnable(){
//            @Override
//            public void run() {
//                int i=4;
//                while (true){
//                    list.remove(i);
//                    i++;
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        Thread thread =new Thread(runnable);
//        Thread thread2 =new Thread(runnable2);
//        try {
//            Thread.sleep(100000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//

    }

}
