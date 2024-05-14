package com.example.demo.Controller;

//
//import jakarta.websocket.*;
//import jakarta.websocket.server.PathParam;
//import jakarta.websocket.server.ServerEndpoint;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException; /**
// * Created on 2021-12-10 16:31
// *
// * @author
// * description：长连接
// */
//@Component
//@Data
//@Slf4j
//@ServerEndpoint(value = "/websocket/{xxx}")
//public class WebSocketController {
//
//    /**
//     * 建立连接时调用
//     * @param session
//     * @param config
//     */
//    @OnOpen
//    public void onOpen(Session session, EndpointConfig config){
//        System.out.println(session + "建立了连接");
//    }
//
//
//    /**
//     * 断开连接时调用
//     * @param session
//     */
//    @OnClose
//    public void onClose(Session session) {
//        System.out.println(session + "断开了连接");
//    }
//
//
//    /**
//     * 消息到达时调用
//     *
//     * @param xxx
//     * @param message
//     * @param session
//     */
//    @OnMessage
//    public void onMessage(@PathParam("xxx") String xxx, String message, Session session){
//        System.out.println(message);
//        try {
//            session.getBasicRemote().sendText(message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 发生错误时调用
//     *
//     * @param session
//     * @param throwable
//     */
//    @OnError
//    public void onError(Session session, Throwable throwable) {
//        System.out.println("发生错误！");
//    }
//}
