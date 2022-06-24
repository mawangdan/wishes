//package cn.edu.xmu.wishes.user.controller;
//
//import cn.edu.xmu.wishes.user.model.Chat;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.user.SimpUserRegistry;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import org.springframework.web.socket.BinaryMessage;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.security.Principal;
//
//@Controller
//public class ChatController {
//    @Autowired
//    private SimpUserRegistry simpUserRegistry;
//
//    @Autowired
//    private SimpMessagingTemplate brokerMessagingTemplate;
//
//    @MessageMapping("/chat/private")
//    public Chat privateChat(SimpMessageHeaderAccessor sha, @Payload Chat content, Principal principal) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        System.out.println(objectMapper.writeValueAsString(content));
//
//        this.brokerMessagingTemplate.convertAndSendToUser(content.getReceiver(), "/topic/chat", content);
//        return content;
//    }
//    @Data
//    class MultipartData{
//        private String sender;
//        private String receiver;
//        private MultipartFile content;
//    }
//    @PostMapping(value = "/chat/file")
//    public Chat file(MultipartFile multipartFile, Principal principal) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        System.out.println(objectMapper.writeValueAsString(multipartFile));
//
////        this.brokerMessagingTemplate.convertAndSendToUser(content.getReceiver(), "/topic/chat", content);
//        return null;
//    }
//
//    @MessageMapping("/chat/public") //这里是客户端发送消息对应的路径，等于configureMessageBroker中配置的setApplicationDestinationPrefixes + 这路径即 /app/sendPublicMessage
//    @SendTo("/topic/chat") //也可以使用 messagingTemplate.convertAndSend(); 推送
//    public BinaryMessage sendPublicMessage(@Payload Chat chatMessage) {
//        File file = new File("C:\\Users\\wl\\Pictures\\下载.jpg");
//        MultipartHttpServletRequest multipartHttpServletRequest;
//        try (FileInputStream fileInputStream = new FileInputStream(file)) {
//            byte bytes[] = new byte[Math.toIntExact(file.length())];
//            fileInputStream.read(bytes);
//            ByteBuffer payload;
//            BinaryMessage binaryMessage = new BinaryMessage(bytes);
//            return binaryMessage;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
////        return chatMessage;
//    }
//}
