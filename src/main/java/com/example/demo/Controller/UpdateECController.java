package com.example.demo.Controller;


import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.Config.AjaxResult;
import com.example.demo.Config.Auth;
import com.example.demo.Config.File;
import com.example.demo.Data.EC;
import com.example.demo.Mapper.VersionMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/update")
public class UpdateECController {
    @Autowired
    File ecFile;
    @Autowired
    VersionMapper versionMapper;
   @Value("${server.port}")
    String  port;
    @Auth(user = "1000")
    @PostMapping("/updateEC")
    public AjaxResult upload(@RequestParam("file") MultipartFile multipartFile, @PathParam("version") String version,
                             @PathParam("message") String message, @PathParam("dialog") Boolean dialog,
                             @PathParam("force") Boolean force) throws IOException {

        try {

            String name = ecFile.addEC(multipartFile);
            if (name == null || name.length() == 0) {
                return AjaxResult.fail(404, "上传文件错误");
            }
            EC ec = new EC();
            ec.setDownload_url("https://luckydbl.top:9500" + name);
            ec.setDialog(true);
            ec.setVersion(version);
            ec.setMsg(message);
            ec.setForce(force);


//            versionMapper.deleteAllVersion();
            if (versionMapper.setVersion(ec)){
                log.info("version:{},message:{},filepath:", version, message);
                return AjaxResult.success();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return AjaxResult.fail(-1, "出错");

    }


    @GetMapping("/getUpdateECVersion")
    public String getVersion(@PathParam("version") String version) throws IOException {
        log.info("version:{}",version);
        EC ecVersion = versionMapper.getNewVersion();
        if (ecVersion == null) {
            return "";
        }
        if (!ecVersion.getVersion().equals(version)) {

            return   JSONUtil.toJsonStr(ecVersion)   ;
        }
        else return "";
    }
}
