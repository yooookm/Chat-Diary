package com.example.diary_chat.controller;

import com.example.diary_chat.domain.ChatMessage;
import com.example.diary_chat.domain.ChatRoom;
import com.example.diary_chat.domain.Diary;
import com.example.diary_chat.domain.Emotion;
import com.example.diary_chat.domain.Member;
import com.example.diary_chat.dto.ChatGPTRequest;
import com.example.diary_chat.dto.ChatGPTResponse;
import com.example.diary_chat.dto.ChatRequest;
import com.example.diary_chat.dto.ChatResponse;
import com.example.diary_chat.dto.DiaryResponse;
import com.example.diary_chat.dto.KeywordsResponse;
import com.example.diary_chat.dto.Message;
import com.example.diary_chat.resolver.LoginUser;
import com.example.diary_chat.service.ChatRoomService;
import com.example.diary_chat.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/chatRooms/{chatRoomId}")
@Tag(name = "CustomBot", description = "CustomBot API")
public class CustomBotController {

    private final RestTemplate template;
    private final DiaryService diaryService;
    private final ChatRoomService chatRoomService;
    @Value("${openai.model}")
    private String model;
    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    public CustomBotController(RestTemplate template, DiaryService diaryService,
                               ChatRoomService chatRoomService) {
        this.template = template;
        this.diaryService = diaryService;
        this.chatRoomService = chatRoomService;
    }

    // AI 채팅
    @PostMapping("/messages")
    @Operation(summary = "AI 채팅", description = "ChatGPT를 사용하여 AI 채팅을 수행합니다.",
            security = @SecurityRequirement(name = "JWT"))
    public ResponseEntity<ChatResponse> chat(@Parameter(hidden = true) @LoginUser Member loginUser,
                                             @PathVariable Long chatRoomId,
                                             @RequestBody ChatRequest chatRequest) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId, loginUser.getId());
        List<Message> chatHistory = chatRoom.getChatHistory().stream()
                .map(chatMessage -> new Message(chatMessage.getRole() == ChatMessage.Role.USER ? "user" : "assistant",
                        chatMessage.getContent()))
                .collect(Collectors.toList());

        String prompt = chatRequest.getPrompt();
        chatHistory.add(new Message("user", prompt));
        chatHistory.add(
                new Message("system", "You are an assistant that helps users by empathizing with their emotions."));
        chatHistory.add(
                new Message("system", "Empathize with the user's emotions and respond kindly and understandingly."));
        chatHistory.add(
                new Message("system", "Acknowledge the emotions and provide appropriate empathy and feedback."));
        chatHistory.add(new Message("system", "Answer in korean."));

        ChatGPTRequest request = new ChatGPTRequest(model, chatHistory);
        ChatGPTResponse response = template.postForObject(apiURL, request, ChatGPTResponse.class);

        String reply = response.getChoices().get(0).getMessage().getContent();

        chatRoom.getChatHistory().add(new ChatMessage(prompt, chatRoom, ChatMessage.Role.USER));
        chatRoom.getChatHistory().add(new ChatMessage(reply, chatRoom, ChatMessage.Role.ASSISTANT));
        chatRoomService.saveChatRoom(chatRoom);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ChatResponse(reply));
    }

    // 일기 생성
    @PostMapping("/diaries")
    @Operation(summary = "일기 생성", description = "채팅 기록을 기반으로 일기를 생성합니다.",
            security = @SecurityRequirement(name = "JWT"))
    public ResponseEntity<DiaryResponse> makeDiary(@Parameter(hidden = true) @LoginUser Member loginUser,
                                                   @PathVariable Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId, loginUser.getId());
        List<Message> chatHistory = diaryService.createChatHistory(chatRoom);

        if (chatHistory == null || chatHistory.isEmpty()) {
            throw new IllegalArgumentException("채팅 기록이 없는 채팅방 입니다.");
        }

        String diaryContent = diaryService.generateDiaryContent(chatHistory);
        String title = diaryService.generateDiaryTitle(diaryContent);
        Emotion emotion = diaryService.extractPrimaryEmotion(diaryContent);
        KeywordsResponse keywordsResponse = diaryService.extractKeywords(diaryContent);

        Diary diary = new Diary(loginUser.getId(), title, diaryContent, chatRoom.getDate(), emotion,
                keywordsResponse.getKeyword1(),
                keywordsResponse.getKeyword2(), keywordsResponse.getKeyword3());

        diaryService.save(diary);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DiaryResponse(diary));

    }
}