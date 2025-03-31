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


//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.atomic.AtomicReference;if (taskData.getPersonAddress() !=null && !taskData.getPersonAddress().isEmpty()){
////解析直播间roomId
//String sec_uid =xiguaAddress.getsecuidBypersonAddress(taskData.getPersonAddress());
//String roomId = xiguaAddress.getRoomIdByPersonAddress(sec_uid);
//            if (roomId == null){
//        return AjaxResult.fail(404,"地址解析错误");
//            }
//AtomicReference<String> videoName = new AtomicReference<>();
//int taskCount = 2;
//CountDownLatch latch = new CountDownLatch(taskCount);
//
//Thread task1 = new Thread(() -> {
//    System.out.println("任务 1 开始");
//    //获取直播人名
//    videoName.set(xiguaAddress.getNickNameByPersonAddress(sec_uid));
//    System.out.println("任务 1 完成");
//    latch.countDown();
//});
//AtomicReference<String> xiguaName = new AtomicReference<>();
//Thread task2 = new Thread(() -> {
//    System.out.println("任务 2 开始");
//    xiguaName.set(xiguaAddress.getXiGuaName(roomId));
//    System.out.println("任务 2 完成");
//    latch.countDown();
//});
//            task1.start();
//            task2.start();
//// 等待所有任务完成
//            latch.await();
//
//            if (videoName.get() == null){
//        return AjaxResult.fail(404,"直播人地址解析错误");
//            }
//                    taskData.setRoomId(roomId);
//            taskData.setVideoName(videoName.get());
//        if (xiguaName.get() == null){
//        xiguaName.set(xiguaAddress.getXiGuaName(roomId));
//        }
//        if (xiguaName.get() != null){
//        taskData.setVideoName(xiguaName.get());
//        taskData.setVideoNameXiGua(xiguaName.get());
//        }
//        }