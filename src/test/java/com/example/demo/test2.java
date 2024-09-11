package com.example.demo;


import com.example.demo.Address.XiguaAddress;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

@SpringBootTest
public class test2 {


    @Autowired
    private XiguaAddress xiguaAddress;

    @Test
    public void test() throws InterruptedException, IOException {
        ChromeOptions options =new ChromeOptions();

        // 设置允许弹框
        options.addArguments("disable-infobars","disable-web-security");
        // 设置无gui 开发时还是不要加，可以看到浏览器效果
        options.addArguments("--headless");
//        String driverPath =  "C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe";
        String driverPath =  "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", driverPath);


        for (int i = 0; i < 5; i++) {
            ChromeDriver driver=  new ChromeDriver(options);
            driver.get("https://www.baidu.com");
//            driver.close();
//           driver.quit();
            sleep(1000);
            driver.quit();
        }

        try {
            // 构建命令：使用任务管理器命令来结束进程
            String command = "taskkill /F /IM chromedriver.exe";

            // 执行命令
            Process process = Runtime.getRuntime().exec(command);

            // 等待命令执行完成
            process.waitFor();

            // 检查进程是否成功终止
            if (process.exitValue() == 0) {
                System.out.println("chromedriver.exe has been terminated.");
            } else {
                System.out.println("Failed to terminate chromedriver.exe.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
