package com.example.demo.Address;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.CharsetUtil;
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

}
