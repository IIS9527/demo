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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
            log.info(e.toString());
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

    @GetMapping("/getPerson")
    public String getPerson(@PathParam("address") String address){

        try {
            HtmlPage page = getTimeByHtmlUnit(address);
            UrlBuilder builder = UrlBuilder.ofHttp(page.getBody().toString(), CharsetUtil.CHARSET_UTF_8);
            log.info(builder.getQuery().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

         return  "true";

    }


    public  String getRoomIdByPersonAddress(String personAddress){

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
//            log.info(document1.outerHtml());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (document1 == null){return null; }
        UrlBuilder urlBuilder =  UrlBuilder.of(document1.getElementsByTag("a").attr("href"));
        String sec_uid= urlBuilder.getQuery().get("sec_uid").toString();

        Map<String,String> userAgent =  new HashMap<String,String>();
        userAgent.put("Host","weixin.qq.com");
        userAgent.put("cookie", "douyin.com; xg_device_score=7.696845314289301; device_web_cpu_core=36; device_web_memory_size=8; architecture=amd64; ttwid=1%7Cbw2fl4da1A6RPEptdVhTJb3-TjFVhpLlTu7yTA1rIDc%7C1711977532%7C4fe861baeb2a41e5a01ce502010f834d0d8717a80811faf9e773090235dd59ea; SEARCH_RESULT_LIST_TYPE=%22single%22; passport_csrf_token=5d6eda75a081ac61534e4336169cc2e3; passport_csrf_token_default=5d6eda75a081ac61534e4336169cc2e3; bd_ticket_guard_client_web_domain=2; xgplayer_user_id=751339677732; live_use_vvc=%22false%22; FORCE_LOGIN=%7B%22videoConsumedRemainSeconds%22%3A180%7D; volume_info=%7B%22isUserMute%22%3Afalse%2C%22isMute%22%3Atrue%2C%22volume%22%3A0.4%7D; odin_tt=f10a8de7373a47cff8de6312bfbf593423631b94dba40b5993574a4a68c89eb78623e259b4a89fa391d53b6a1c92ad912d6f0e58e59da3614811d0dd745f5116e5312491804ec6258ffbe8bbad6098e8; download_guide=%223%2F20240426%2F0%22; stream_player_status_params=%22%7B%5C%22is_auto_play%5C%22%3A0%2C%5C%22is_full_screen%5C%22%3A0%2C%5C%22is_full_webscreen%5C%22%3A0%2C%5C%22is_mute%5C%22%3A1%2C%5C%22is_speed%5C%22%3A1%2C%5C%22is_visible%5C%22%3A0%7D%22; pwa2=%220%7C0%7C3%7C0%22; __live_version__=%221.1.1.9998%22; webcast_leading_last_show_time=1714722830262; webcast_leading_total_show_times=1; webcast_local_quality=sd; live_can_add_dy_2_desktop=%221%22; dy_swidth=1920; dy_sheight=1080; csrf_session_id=b3206be87a22fa7f6ea1b486995bea46; strategyABtestKey=%221714722938.083%22; __ac_nonce=06634a16500be75947a72; __ac_signature=_02B4Z6wo00f01boTcxAAAIDBMNTBcjMBikG6M3eAAAix8f; stream_recommend_feed_params=%22%7B%5C%22cookie_enabled%5C%22%3Atrue%2C%5C%22screen_width%5C%22%3A1920%2C%5C%22screen_height%5C%22%3A1080%2C%5C%22browser_online%5C%22%3Atrue%2C%5C%22cpu_core_num%5C%22%3A36%2C%5C%22device_memory%5C%22%3A8%2C%5C%22downlink%5C%22%3A5.3%2C%5C%22effective_type%5C%22%3A%5C%224g%5C%22%2C%5C%22round_trip_time%5C%22%3A0%7D%22; home_can_add_dy_2_desktop=%221%22; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWl0ZXJhdGlvbi12ZXJzaW9uIjoxLCJiZC10aWNrZXQtZ3VhcmQtcmVlLXB1YmxpYy1rZXkiOiJCSFRIYm1lNjB5d0ptd09Hb05WZDlLV1BEUWhLRGdySEtuZGVTUlc4aUJlL241ajNieHIxMjBHeW1VTitKQTZlQmVRV3NRQzhEdWlnc294TUlyKzF1aEE9IiwiYmQtdGlja2V0LWd1YXJkLXdlYi12ZXJzaW9uIjoxfQ%3D%3D; msToken=H3eaE_kegTUPujqKpSZupkrsBddQowGn3UCWbEOVUUxmqP3tjbmUwj8i08Z7ME0v4R39J8C6Am1XiUiiyOLeZAiR44b-rfVeXM_q2IGE3UkM84S9NdB6gXWHMUHO2O4b; IsDouyinActive=false");
        userAgent.put("Cache-Control","max-age=0");
        userAgent.put("Sec-Ch-Ua","\"Chromium\";v=\"112\", \"Microsoft Edge\";v=\"112\", \"Not:A-Brand\";v=\"99\"");
        userAgent.put("Sec-Ch-Ua-Mobile","0");
        userAgent.put("Sec-Ch-Ua-Platfo","\"Windows\"");
        userAgent.put("Upgrade-Insecure-Reques","1");
        userAgent.put("User-Agen","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.58");
        userAgent.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        userAgent.put("Sec-Fetch-Sit","none");
        userAgent.put("Sec-Fetch-Mod","navigate");
        userAgent.put("Sec-Fetch-Use","1");
        userAgent.put("Accept-Encoding","gzip, deflate");
        userAgent.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        Document document = null;
        try {
            document = Jsoup.connect("https://www.douyin.com/user/"+sec_uid).headers(userAgent).timeout(2500).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Elements e = document.getElementsByTag("script");
        for (Element script : e) {
            // 获取每个<script>元素内部内容
            String scriptContent = script.html();
            // 检查是否包含"myFunction"这个名字
            if (scriptContent.contains("roomIdStr")) {
                String[] findRoomidStr= scriptContent.split("roomIdStr\\\\\":\\\\\"");
                if (findRoomidStr.length>1){
                    System.out.println("sssssssssssssssssssssssss"+findRoomidStr[1]);
                    System.out.println(findRoomidStr[1].split("\\\\")[0]);
                    if ( findRoomidStr[1].split("\\\\")[0] != null &&!"0".equals(findRoomidStr[1].split("\\\\")[0]) && NumberUtil.isNumber(findRoomidStr[1].split("\\\\")[0])){
                        return     findRoomidStr[1].split("\\\\")[0];
                    }
                }
            }
        }
        return null;
    }

    /**
     *
     * @param personAddress
     * @return null || nickName
     */
    public  String getNickNameByPersonAddress(String personAddress){
        Map<String,String> userAgent =  new HashMap<String,String>();
        userAgent.put("Accept","charset=utf-8");
        userAgent.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        userAgent.put("Cache-Control","max-age=0");
        userAgent.put("Content-Type","application/json; charset=utf-8");
        userAgent.put("Sec-Ch-Ua","Microsoft Edge\";v=\"117\", \"Not;A=Brand\";v=\"8\", \"Chromium\";v=\"117\"");
        userAgent.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 Edg/117.0.2045.43");
        Document document = null;
        try {
            document = Jsoup.connect(personAddress).headers(userAgent).followRedirects(false).timeout(2500).get();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (document == null){return null; }
        UrlBuilder urlBuilder =  UrlBuilder.of(document.getElementsByTag("a").attr("href"));

        String sec_uid= urlBuilder.getQuery().get("sec_uid").toString();
        try {
            Document document1 = Jsoup.connect("https://www.iesdouyin.com/web/api/v2/user/info/?sec_uid="+sec_uid+"&from_ssr=1").headers(userAgent).followRedirects(false).ignoreContentType(true).timeout(2500).get();
//            System.out.println(document1.getElementsByTag("body"));
            JSONObject jsonObject = JSONUtil.parseObj(document1.getElementsByTag("body").text());
//            System.out.println(jsonObject);
            String nickName = jsonObject.getJSONObject("user_info").get("nickname").toString();
            return     StrUtil.isEmptyIfStr(nickName) ? null : nickName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getXiGuaName(String roomid) {

        ChromeOptions options =new ChromeOptions();

        // 设置允许弹框
        options.addArguments("disable-infobars","disable-web-security");
        // 设置无gui 开发时还是不要加，可以看到浏览器效果
        options.addArguments("--headless");
        String driverPath =  "C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe";
//        String driverPath =  "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe";


        System.setProperty("webdriver.chrome.driver", driverPath);

        HashMap<String,String>  mobileEmulation = new HashMap<String,String>();
        mobileEmulation.put("deviceName","iPhone XR");
        options.setExperimentalOption("mobileEmulation", mobileEmulation);

        ChromeDriver driver=  new ChromeDriver(options);

        driver.get("https://webcast-open.douyin.com/open/webcast/reflow/?webcast_app_id=247160&room_id="+roomid);


        new WebDriverWait(driver, Duration.ofMinutes(1)).until(ExpectedConditions.presenceOfElementLocated(By.className("saas-reflow-room-anchor-name")));

        String   xiguaName =  driver.findElement(By.className("saas-reflow-room-anchor-name")).getAttribute("textContent");

        log.info("xiguaName find is :{}", xiguaName);

        driver.close();

        return xiguaName;

    }


}
