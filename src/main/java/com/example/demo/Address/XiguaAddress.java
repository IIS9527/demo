package com.example.demo.Address;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.DefaultJavaScriptErrorListener;

import io.micrometer.common.util.StringUtils;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/*
*
* 是抖音的地址解析
* */

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class XiguaAddress {


    @Autowired
    public  DouyinRoomInfo douyinRoomInfo;


    String IP = "http://110.40.201.109:8998";
    public String getRoomId( String address){
        try  {


            HtmlPage page =getTimeByHtmlUnit(address);

            log.info(page.getBaseURI());
            String reg = "(?<=reflow/).*?(?=\\?u_code)";
            String temp = "aAabcBc";
            Pattern pattern = Pattern.compile(reg);
            log.info(page.getBaseURI().toString());
            Matcher matcher = pattern.matcher(page.getBaseURI());
            if( matcher.find() ){
                System.out.println(matcher.group());
                return  matcher.group();
            }
            else {

                List<HtmlElement> p =  page.getBody().getElementsByAttribute("a","class","B3AsdZT9");


                 reg = "(?<=room_id=).*?(?=&)";
                 pattern = Pattern.compile(reg);
//                 matcher = pattern.matcher(roomid);
                if (matcher.find()){
                    System.out.println("ssssssssss"+matcher.group());
                    return  matcher.group();
                }
            }

            return "false";

        }
        catch ( Exception e){
            log.error("getRoomId Exception:{}",e.toString());
            return "false";
        }

    }

//    }
    public String getVideoName( String address){
        try  {

            HtmlPage page =getTimeByHtmlUnit(address);

            if (page == null) return "false";
            log.info("1:{}",page.getBaseURI());

            UrlBuilder builder = UrlBuilder.ofHttp(page.getBaseURI(), CharsetUtil.CHARSET_UTF_8);

            log.info("2{}",builder.getQuery().toString());
            String sec_uid = null;
            if (StrUtil.isEmptyIfStr( builder.getQuery().get("sec_user_id"))){
              log.info("3{}",builder.getPathStr().split("/")[builder.getPathStr().split("/").length-1]);
                  sec_uid =  builder.getPathStr().split("/")[builder.getPathStr().split("/").length-1];
             }

             else {
                  sec_uid =  builder.getQuery().get("sec_user_id").toString();
             }

            String url= "https://www.iesdouyin.com/web/api/v2/user/info/?sec_uid="+sec_uid+"&from_ssr=1";

            WebClient webClient = new WebClient(BrowserVersion.getDefault());

            JSONObject jsonObject =  JSONUtil.parseObj(webClient.getPage(url).getWebResponse().getContentAsString(StandardCharsets.UTF_8));

            log.info(  "全部参数:{}",  JSONUtil.parseObj (jsonObject));
            log.info(  "直播人名称:{}",  JSONUtil.parseObj (jsonObject.get("user_info")).get("nickname"));

            return JSONUtil.parseObj (jsonObject.get("user_info")).get("nickname").toString();
        }

        catch ( Exception e){
            log.error(e.toString());
            return "false";
        }
    }

    public HtmlPage getTimeByHtmlUnit(String url) throws IOException {

        if (StringUtils.isBlank(url)) {
            return null;
        }

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(6000);

        // -----重点-----设置为我们自定义的错误处理类

        webClient.setJavaScriptErrorListener(new MyJSErrorListener());

        webClient.setJavaScriptTimeout(5000);

        HtmlPage page =webClient.getPage(url);

        webClient.waitForBackgroundJavaScript(6000);
        //wait for js execute

        //这里是我的爬取目标 忽略即可
        return page;
    }


    /**
     * 忽略html unit打印的所有js加载报错信息
     */
    public class MyJSErrorListener extends DefaultJavaScriptErrorListener {
        @Override
        public void scriptException(HtmlPage page, ScriptException scriptException) {
        }

        @Override
        public void timeoutError(HtmlPage page, long allowedTime, long executionTime) {
        }

        @Override
        public void malformedScriptURL(HtmlPage page, String url, MalformedURLException malformedURLException) {

        }

        @Override
        public void loadScriptError(HtmlPage page, URL scriptUrl, Exception exception) {

        }

        @Override
        public void warn(String message, String sourceName, int line, String lineSource, int lineOffset) {

        }
    }



    public String getRoomIdByPersonAddress(String sec_uid){
        Document document = null;
        try {
            document = Jsoup.connect(IP+"/dy/getRoomIdByPersonAddress?sec_uid="+sec_uid).get();
            return document.body().html();
        } catch (IOException e) {
            log.error("getPerson IOException:{}",e.toString());
            return null;
        }

    }

//    public  String getRoomIdByPersonAddress(String sec_uid){
//
//
//        Map<String,String> userAgent =  new HashMap<String,String>();
//        userAgent.put("Host","weixin.qq.com");
//        userAgent.put("cookie", "__ac_nonce=066f66d7300e5ba89b61; __ac_signature=_02B4Z6wo00f01CHLorgAAIDB.pBR9eOH-Mwh66YAAG6Lb6; __ac_referer=https://www.iesdouyin.com/");
//    //        userAgent.put("cookie", "douyin.com; __ac_referer=__ac_blank; UIFID_TEMP=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e3852804ed5c4c0c86fd63c6a711c525d0315c3c149475f7fbee91673ffc052544f7b49717be24e06f5511a122d1394b99f3696; ttwid=1%7Ca3AiqHUX6fWrXmXzoa0ko5WQ8JNm_YOJaUjLBbqfpPc%7C1723598657%7C93e1b5fbc326eb69b778766dbc2635fd7fd38e6216d52988a624f39e243a365f; bd_ticket_guard_client_web_domain=2; passport_csrf_token=e46c7f044d8e1f4d7b60b0d2ae301229; passport_csrf_token_default=e46c7f044d8e1f4d7b60b0d2ae301229; douyin.com; device_web_cpu_core=12; device_web_memory_size=8; architecture=amd64; fpk1=U2FsdGVkX1+/It9FqDGZzVkRrsqLjtNh+wOvwdbJFpGrMEci781SMihFi8Hi4th2XwUNQ9y04AxQnjMfFwm/HA==; fpk2=a565ccc5e7018c4ec7bec64e38db2966; csrf_session_id=916c42af3096c4c47c935cca540a29fa; __live_version__=%221.1.2.3141%22; webcast_local_quality=null; live_use_vvc=%22false%22; webcast_leading_last_show_time=1724805926048; webcast_leading_total_show_times=1; s_v_web_id=verify_m0d4wzlw_d3fd9862_2cd9_30fc_ffbb_327ffc1fd578; UIFID=c3109cf8eab4507640f022360c5ce002c7035d0857c7085fdeb180d1661fca19afd0c7769bff0c457283f1db8ddf8b95f3dc71b7e33f8d18039a83d9794c69cb08acc03fd7930c8981a002c590b64ebfd78ea3c9e9beed3628cff826efdc6349179f5df312e012a81a02f23ba1ea2238de677303a1253812fb07aa5a704055f2abe06105a94f224f6504a140311fa90f9b2056532c2ef03174f1638b54632e0b; odin_tt=ea1b6f908fffaa566129aba37a417813090c2cf690e982dd3b5741258282b712f6c159929a2998fda1160ef60d109ba6acedc5c57d335496d3a956f9616995714f887e8da0c491ca003e6b575d14947c; __ac_nonce=066f6644900f2f4b0ff87; __ac_signature=_02B4Z6wo00f01UM62NQAAIDAnGErmsVtICVDGtxAADY8b9; dy_swidth=1536; dy_sheight=864; strategyABtestKey=%221727423564.559%22; FORCE_LOGIN=%7B%22videoConsumedRemainSeconds%22%3A180%7D; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWl0ZXJhdGlvbi12ZXJzaW9uIjoxLCJiZC10aWNrZXQtZ3VhcmQtcmVlLXB1YmxpYy1rZXkiOiJCTEd6ZFh5TXdYZ0x5cFYrSzJsdjNXb09rd2VyK0NQUVg2a1k3VXpBTDR6M3dYa1pSSTFBQnhlNmZWR1NobzlOZXVXZW02MW5zdURZNk4yZU9nb3NMWms9IiwiYmQtdGlja2V0LWd1YXJkLXdlYi12ZXJzaW9uIjoxfQ%3D%3D; download_guide=%221%2F20240927%2F0%22; home_can_add_dy_2_desktop=%220%22; stream_recommend_feed_params=%22%7B%5C%22cookie_enabled%5C%22%3Atrue%2C%5C%22screen_width%5C%22%3A1536%2C%5C%22screen_height%5C%22%3A864%2C%5C%22browser_online%5C%22%3Atrue%2C%5C%22cpu_core_num%5C%22%3A12%2C%5C%22device_memory%5C%22%3A8%2C%5C%22downlink%5C%22%3A0.35%2C%5C%22effective_type%5C%22%3A%5C%222g%5C%22%2C%5C%22round_trip_time%5C%22%3A1950%7D%22; IsDouyinActive=false; biz_trace_id=a3231a25");
//        userAgent.put("Cache-Control","max-age=0");
//        userAgent.put("Sec-Ch-Ua","\"Chromium\";v=\"112\", \"Microsoft Edge\";v=\"112\", \"Not:A-Brand\";v=\"99\"");
//        userAgent.put("Sec-Ch-Ua-Mobile","0");
//        userAgent.put("Sec-Ch-Ua-Platfo","\"Windows\"");
//        userAgent.put("Upgrade-Insecure-Reques","1");
//        userAgent.put("User-Agen","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.58");
//        userAgent.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
//        userAgent.put("Sec-Fetch-Sit","none");
//        userAgent.put("Sec-Fetch-Mod","navigate");
//        userAgent.put("Sec-Fetch-Use","1");
//        userAgent.put("Accept-Encoding","gzip, deflate");
//        userAgent.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
//        Document document = null;
//        try {
//            document = Jsoup.connect("https://www.douyin.com/user/"+sec_uid).headers(userAgent).timeout(5500).get();
//        } catch (IOException e) {
//           log.error("getPerson IOException:{}",e.toString());
//            return null;
//        }
////        log.error("document2:{}",document.);
//        Elements e = document.getElementsByTag("script");
//        for (Element script : e) {
//            // 获取每个<script>元素内部内容
//            String scriptContent = script.html();
//            // 检查是否包含"myFunction"这个名字
//            if (scriptContent.contains("roomIdStr")) {
//                log.error("document3:"+scriptContent);
//                String[] findRoomidStr= scriptContent.split("roomIdStr\\\\\":\\\\\"");
//                if (findRoomidStr.length>1){
//                    System.out.println("sssssssssssssssssssssssss"+findRoomidStr[1]);
//                    System.out.println(findRoomidStr[1].split("\\\\")[0]);
//                    if ( findRoomidStr[1].split("\\\\")[0] != null &&!"0".equals(findRoomidStr[1].split("\\\\")[0]) && NumberUtil.isNumber(findRoomidStr[1].split("\\\\")[0])){
//                        log.error("document3:"+findRoomidStr[1].split("\\\\")[0]);
//                        return     findRoomidStr[1].split("\\\\")[0];
//                    }
//                }
//            }
//        }
//        log.error("document3:null");
//        return null;
//    }

//    public  String getsecuidBypersonAddress(String personAddress){
//        Map<String,String> userAgent1 =  new HashMap<String,String>();
//        userAgent1.put("Accept","charset=utf-8");
//        userAgent1.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
//        userAgent1.put("Cache-Control","max-age=0");
//        userAgent1.put("Content-Type","application/json; charset=utf-8");
//        userAgent1.put("Sec-Ch-Ua","Microsoft Edge\";v=\"117\", \"Not;A=Brand\";v=\"8\", \"Chromium\";v=\"117\"");
//        userAgent1.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 Edg/117.0.2045.43");
//        Document document1 = null;
//        try {
//            document1 = Jsoup.connect(personAddress).headers(userAgent1).followRedirects(false).timeout(2500).get();
//
//        } catch (IOException e) {
//            log.error(e.toString());
//            return null;
//        }
//        if (document1 == null){return null; }
//        log.error("document{}",document1.outerHtml());
//        UrlBuilder urlBuilder =  UrlBuilder.of(document1.getElementsByTag("a").attr("href"));
//        return urlBuilder.getQuery().get("sec_uid").toString();
//    }

    public  String getsecuidBypersonAddress(String personAddress){

        Document document = null;
        try {
            document = Jsoup.connect(IP+"/dy/getsecuid?personAddress="+personAddress).get();
            return document.body().html();
        } catch (IOException e) {
            log.error("getPerson IOException:{}",e.toString());
        return null;
        }
    }

    public  String getRoomIdByPersonAddress1(String personAddress){

        Map<String,String> userAgent1 =  new HashMap<String,String>();
        userAgent1.put("Accept","charset=utf-8");
        userAgent1.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        userAgent1.put("Cache-Control","max-age=0");
        userAgent1.put("Content-Type","application/json; charset=utf-8");
        userAgent1.put("Sec-Ch-Ua","Microsoft Edge\";v=\"117\", \"Not;A=Brand\";v=\"8\", \"Chromium\";v=\"117\"");
        userAgent1.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 Edg/117.0.2045.43");
        Document document1 = null;
        try {
            document1 = Jsoup.connect(personAddress).headers(userAgent1).followRedirects(false).timeout(2500).get();

        } catch (IOException e) {
            log.error(e.toString());
            return null;
        }
        if (document1 == null){return null; }
        log.error("document{}",document1.outerHtml());
        UrlBuilder urlBuilder =  UrlBuilder.of(document1.getElementsByTag("a").attr("href"));
        String sec_uid= urlBuilder.getQuery().get("sec_uid").toString();
        log.error("sec_uid{}",sec_uid);

//        browser.get('https://www.douyin.com/user/MS4wLjABAAAALyB3O1iOimQh_rx9mpLB6b1dcfW48DaOz9ZfhKWSqsrw3IZ7VoPeYl9bCtmYn9QU')
//
//        WebDriverWait(browser, 200).until(
//                EC.presence_of_element_located((By.XPATH, "//script[contains(@crossorigin, 'anonymous') and contains(text(), 'roomIdStr')]"))
//)
        // 设置 GeckoDriver 路径
        System.setProperty("webdriver.gecko.driver", "/usr/bin/geckodriver");

        // 创建 FirefoxOptions 对象，用于配置浏览器选项
        FirefoxOptions options = new FirefoxOptions();
        // 创建一个 map 用于模拟手机设备的 UA 信息
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // 启动 WebDriver
        WebDriver driver = new FirefoxDriver(options);

        driver.get("https://www.douyin.com/user/"+sec_uid);
        WebElement element = new WebDriverWait(driver, Duration.ofMinutes(10)).until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//script[@crossorigin='anonymous' and contains(text(), 'roomIdStr')]")));

        driver.findElement(By.xpath("//script[@crossorigin='anonymous' and contains(text(), 'roomIdStr')]"));
        log.info("1111111{}",driver.getPageSource());
        String[] findRoomidStr1 = driver.getPageSource().split("roomIdStr\\\\\":\\\\\"");
        log.info("findRoomidStr1{}",findRoomidStr1);
        if (findRoomidStr1.length>1){
            System.out.println("sssssssssssssssssssssssss"+findRoomidStr1[1]);
            System.out.println(findRoomidStr1[1].split("\\\\")[0]);
            if ( findRoomidStr1[1].split("\\\\")[0] != null &&!"0".equals(findRoomidStr1[1].split("\\\\")[0]) && NumberUtil.isNumber(findRoomidStr1[1].split("\\\\")[0])){
                return     findRoomidStr1[1].split("\\\\")[0];
            }
        }
        driver.quit();
        log.error("document3:null");
        return null;
    }
    /**
     *
     * @param sec_uid
     * @return null || nickName
     */
//    public  String getNickNameByPersonAddress(String sec_uid){
//        Map<String,String> userAgent =  new HashMap<String,String>();
//        userAgent.put("Accept","charset=utf-8");
//        userAgent.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
//        userAgent.put("Cache-Control","max-age=0");
//        userAgent.put("Content-Type","application/json; charset=utf-8");
//        userAgent.put("Sec-Ch-Ua","Microsoft Edge\";v=\"117\", \"Not;A=Brand\";v=\"8\", \"Chromium\";v=\"117\"");
//        userAgent.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 Edg/117.0.2045.43");
//        try {
//            Document document1 = Jsoup.connect("https://www.iesdouyin.com/web/api/v2/user/info/?sec_uid="+sec_uid+"&from_ssr=1").headers(userAgent).followRedirects(false).ignoreContentType(true).timeout(2500).get();
////            System.out.println(document1.getElementsByTag("body"));
//            JSONObject jsonObject = JSONUtil.parseObj(document1.getElementsByTag("body").text());
////            System.out.println(jsonObject);
//            String nickName = jsonObject.getJSONObject("user_info").get("nickname").toString();
//            return     StrUtil.isEmptyIfStr(nickName) ? null : nickName;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }

    public String getNickNameByPersonAddress(String sec_uid){
        Document document = null;
        try {
            document = Jsoup.connect(IP+"/dy/getnickNameBySec_uid?sec_uid="+sec_uid).get();
            return document.body().html();
        } catch (IOException e) {
            log.error("getPerson IOException:{}",e.toString());
            return null;
        }

    }
    public String getXiGuaName(String roomid) {

        ChromeOptions options =new ChromeOptions();
//        FirefoxOptions firefoxOptions =new FirefoxOptions();
        // 设置允许弹框
        options.addArguments("disable-infobars","disable-web-security");
//        options.addArguments("--window-size=500,1000");
//        options.addArguments("--auto-open-devtools-for-tabs");
//        firefoxOptions.addArguments("disable-infobars","disable-web-security");

        // 设置无gui 开发时还是不要加，可以看到浏览器效果
        options.addArguments("--headless");
//        firefoxOptions.addArguments("--headless");
//        options.addArguments("--no-sandbox");
//        firefoxOptions.addArguments("--no-sandbox");
//        options.addArguments('--disable-gpu');
//        options.addArguments('--disable-dev-shm-usage');

//        String driverPath =  "C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe";
//        String driverPath =  "/usr/bin/chromedriver";
//        String driverPath =  "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe";
        String driverPath =  "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe";


        System.setProperty("webdriver.chrome.driver", driverPath);

        HashMap<String,String>  mobileEmulation = new HashMap<String,String>();
        mobileEmulation.put("deviceName","iPhone XR");
//
////        mobileEmulation.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36");
////        mobileEmulation.put("sec-ch-ua-mobile","?0");
////        mobileEmulation.put("sec-ch-ua","Not(A:Brand\";v=\"99\", \"Google Chrome\";v=\"133\", \"Chromium\";v=\"133");
//
        options.setExperimentalOption("mobileEmulation", mobileEmulation);
//        firefoxOptions.addArguments("-moz-mobile");


//        Map<String, Object> deviceMetrics = new HashMap<>();
//        deviceMetrics.put("width", 375);       // 设备宽度（像素）
//        deviceMetrics.put("height", 812);      // 设备高度
//        deviceMetrics.put("pixelRatio", 3.0);  // 像素密度（如 iPhone X 为 3.0）
//        deviceMetrics.put("touch", true);           // 启用触摸事件模拟
//        deviceMetrics.put("enableViewport", true);  // 启用视口约束
//        Map<String, Object> mobileEmulation = new HashMap<>();
//        mobileEmulation.put("deviceMetrics", deviceMetrics);
//        mobileEmulation.put("userAgent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
//        options.setExperimentalOption("mobileEmulation", mobileEmulation);



        ChromeDriver driver=  new ChromeDriver(options);

        driver.get("https://webcast-open.douyin.com/open/webcast/reflow/?webcast_app_id=247160&room_id="+roomid);

        new WebDriverWait(driver, Duration.ofMinutes(1)).until(ExpectedConditions.presenceOfElementLocated(By.className("saas-reflow-room-anchor-name")));

        String   xiguaName =  driver.findElement(By.className("saas-reflow-room-anchor-name")).getAttribute("textContent");

        log.info("xiguaName find is :{}", xiguaName);

        driver.quit();

        try {
            // 批处理命令
            String command = "cmd /c taskkill /IM chrome.exe /F >nul 2>&1";

            // 执行命令
            Process process = Runtime.getRuntime().exec(command);

            // 等待命令执行完成
            int exitCode = process.waitFor();

            // 输出命令执行结果
            System.out.println("命令执行完成，退出码: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


        return xiguaName;

    }
    public  String  getXiGuaNameLinux(String roomId){
        // 设置 GeckoDriver 路径
        System.setProperty("webdriver.gecko.driver", "/usr/bin/geckodriver");

        // 创建 FirefoxOptions 对象，用于配置浏览器选项
        FirefoxOptions options = new FirefoxOptions();


        // 创建一个 map 用于模拟手机设备的 UA 信息
        Map<String, Object> deviceMetrics = new HashMap<>();
        deviceMetrics.put("width", 360);
        deviceMetrics.put("height", 640);
        deviceMetrics.put("pixelRatio", 3.0);

        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceMetrics", deviceMetrics);
        mobileEmulation.put("userAgent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.4 Mobile/15E148 Safari/604.1");

        // 将手机模拟选项添加到 FirefoxOptions 中
        options.addPreference("general.useragent.override", mobileEmulation.get("userAgent").toString());
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // 启动 WebDriver
        WebDriver driver = new FirefoxDriver(options);

        driver.get("https://webcast-open.douyin.com/open/webcast/reflow/?webcast_app_id=247160&room_id="+roomId);


        new WebDriverWait(driver, Duration.ofMinutes(1)).until(ExpectedConditions.presenceOfElementLocated(By.className("saas-reflow-room-anchor-name")));

        String   xiguaName =  driver.findElement(By.className("saas-reflow-room-anchor-name")).getAttribute("textContent");

        log.info("xiguaName find is :{}", xiguaName);


        // 关闭浏览器
        driver.quit();
        return xiguaName;
    }


//    public String getXiGuaName(String roomId){
//        String name = douyinRoomInfo.tryMethod1(roomId);
//        if (name != null){
//            return name;
//        }else {
//            return  douyinRoomInfo.tryMethod2(roomId);
//        }
//    }
}




