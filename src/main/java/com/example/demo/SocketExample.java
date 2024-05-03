package com.example.demo;

import java.io.*;
import java.net.*;

public class SocketExample {
    public static String sendMessage(String host, int port, String message) {
        try {
            // 创建Socket连接
            Socket socket = new Socket(host, port);

            // 获取输出流
            OutputStream outputStream = socket.getOutputStream();

            // 创建一个写入器以便发送消息
            PrintWriter writer = new PrintWriter(outputStream, true);

            // 发送消息
            writer.println(message);

            // 关闭Socket连接
            socket.close();

            return "Message sent successfully";
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}
