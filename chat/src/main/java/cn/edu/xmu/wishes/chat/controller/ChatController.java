package cn.edu.xmu.wishes.chat.controller;

import cn.edu.xmu.wishes.chat.model.Chat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    private SimpMessagingTemplate brokerMessagingTemplate;

    @MessageMapping("/chat/private")
    public Chat privateChat(Chat content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(content));

        this.brokerMessagingTemplate.convertAndSendToUser(content.getReceiver(), "/topic/chat", content);
        return content;
    }
}
