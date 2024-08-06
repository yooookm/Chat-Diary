package com.example.diary_chat.dto;

public class ChatRoomResponse {
    private Long userId;
    private Long chatRoomId;
    private String title;

    public ChatRoomResponse(Long userId, Long chatRoomId, String title) {
        this.userId = userId;
        this.chatRoomId = chatRoomId;
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public String getTitle() {
        return title;
    }
}
