package com.example.demo.Config;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class File {

    @Value("${file.uploadUrlQR}")
    private String  fileUrlQR;
    @Value("${file.uploadUrlEC}")
    private String  fileUrlEC;
    @Value("${file.uploadUrlScreenImg}")
    private String  fileUrlScreenImg;







    public String addEC(MultipartFile file) throws Exception{
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println("localhost: " + localHost);
        System.out.println("getHostAddress:  " + localHost.getHostAddress());
        System.out.println("getHostName:  " + localHost.getHostName());

        String originalFilename = file.getOriginalFilename(); //获取文件名
        String type = FileUtil.extName(originalFilename);//
        long size = file.getSize();//大小

        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type; //41b1076684904f9cb4a503fb028db94b.jpg
        java.io.File uploadFile = new java.io.File(fileUrlEC + fileUUID);
        //先存储到磁盘
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取文件的md5
        //使用m5 避免重复上传相同的文件
//        String  md5 = SecureUtil.md5(uploadFile);

        return  "/EC/"+fileUUID;
    }

    public String addUserQRImg(MultipartFile file) throws Exception{
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println("localhost: " + localHost);
        System.out.println("getHostAddress:  " + localHost.getHostAddress());
        System.out.println("getHostName:  " + localHost.getHostName());



        String originalFilename = file.getOriginalFilename(); //获取文件名
        String type = FileUtil.extName(originalFilename);//
        long size = file.getSize();//大小



        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type; //41b1076684904f9cb4a503fb028db94b.jpg
        java.io.File uploadFile = new java.io.File(fileUrlQR + fileUUID);
        //先存储到磁盘
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取文件的md5
        //使用m5 避免重复上传相同的文件
//        String  md5 = SecureUtil.md5(uploadFile);

        return "/user/qrImg/"+fileUUID;
    }


    public String adddeviceScreenImg(MultipartFile file, String roomId,String videoName,String deviceId) {

        String originalFilename = file.getOriginalFilename(); //获取文件名

        String type = FileUtil.extName(originalFilename);//

        Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
        Matcher matcher = pattern.matcher(videoName);
        videoName= matcher.replaceAll("");


        long size = file.getSize();//大小

        String uuid = IdUtil.fastSimpleUUID();

        String fileUUID = deviceId+"kkkkkk"+uuid + StrUtil.DOT + type; //41b1076684904f9cb4a503fb028db94b.jpg


        java.io.File uploadFile = new java.io.File(fileUrlScreenImg +roomId+videoName+"/"+ fileUUID);

        //先存储到磁盘
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取文件的md5
        //使用m5 避免重复上传相同的文件
//        String  md5 = SecureUtil.md5(uploadFile);

//         return "true";
        return "/screen/"+roomId+videoName+"/"+ fileUUID;
    }
}
