package com.example.diary_chat.service;


import com.example.diary_chat.dto.ChatGPTRequest;
import com.example.diary_chat.dto.ChatGPTResponse;
import com.example.diary_chat.dto.Message;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGPTService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate template;

    public ChatGPTService(RestTemplate template) {
        this.template = template;
    }

    public ChatGPTResponse getChatGPTResponse(List<Message> messages) {
        ChatGPTRequest request = new ChatGPTRequest(model, messages);
        return template.postForObject(apiURL, request, ChatGPTResponse.class);
    }
}
