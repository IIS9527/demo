package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 Socket 连接客户端 参数：服务端ip, 端口，shell命令，服务发送接口
 */
@Slf4j
public class SocketClient {

    private String TAG = "SocketClient";
    private PrintWriter printWriter;        //发送用的输出流
    private onServiceSend mOnServiceSend;   //发送给服务器的接口
    private BufferedReader bufferedReader;  //接收输入流
    private String ip;
    private int port;
    private String cmd;                     //执行命令


    public SocketClient(String hostname,String SocketPort, String commod) {
        ip = hostname;
        port = Integer.parseInt(SocketPort);
        cmd = commod;

        try {
            log.info(TAG+ "与service进行socket通讯,地址="+ip+":" + port);
            /** 创建Socket*/
            // 创建一个流套接字并将其连接到指定 IP 地址的指定端口号(本处是本机)
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip,port), 3000);//设置连接请求超时时间3 s
            // 接收3s超时
            socket.setSoTimeout(3000);
            log.info(TAG+"与service进行socket通讯,超时为：" + 3000);
            /** 发送客户端准备传输的信息 */
            // 由Socket对象得到输出流，并构造PrintWriter对象
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            /** 用于获取服务端传输来的信息 */
            // 由Socket对象得到输入流，并构造相应的BufferedReader对象
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new CreateServerThread(socket);
            send(cmd);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(TAG+"与service进行socket通讯发生错误" + e);

        }
    }

    //线程类
    class CreateServerThread extends Thread {
        Socket socket;
        InputStreamReader inputStreamReader;
        BufferedReader reader;
        public CreateServerThread(Socket s) throws IOException {
            log.info(TAG+ "创建了一个新的连接线程");
            socket = s;
            this.start();
        }

       @Override
        public void run() {
            try {
                // 打印读入一字符串并回调
                try {
                    inputStreamReader = new InputStreamReader(socket.getInputStream());
                    reader = new BufferedReader(inputStreamReader);
                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    //直到读完为止
                    while ((line = reader.readLine()) != null) {
                        System.out.println("ypktest=>>" + line);
                        sb.append(line);
                    }
                    log.info(TAG+ "客户端接收解析服务器的while循环结束"+sb);
                } catch (Exception e){
                    e.printStackTrace();
                    log.info(TAG+ "客户端接收解析服务器的Threadcatch块执行：" + e.toString());
                }
                bufferedReader.close();
                printWriter.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.info(TAG+ "socket 接收线程发生错误：" + e.toString());
            }
        }
    }


    public void send(String cmd){
        printWriter.println(cmd );
        // 刷新输出流，使Server马上收到该字符串
        printWriter.flush();
    }

    public interface onServiceSend{
        void getSend(String result);
    }
}