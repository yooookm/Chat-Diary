package com.example.diary_chat.controller;

import com.example.diary_chat.domain.ChatRoom;
import com.example.diary_chat.domain.Member;
import com.example.diary_chat.dto.ChatRoomRequest;
import com.example.diary_chat.dto.ChatRoomResponse;
import com.example.diary_chat.resolver.LoginUser;
import com.example.diary_chat.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatRoom")
@Tag(name = "ChatRoom", description = "ChatRoom API")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping
    @Operation(summary = "채팅방 생성",
            security = @SecurityRequirement(name = "JWT"))
    public ResponseEntity<ChatRoomResponse> addChatRoom(@Parameter(hidden = true) @LoginUser Member loginUser,
                                                        @RequestBody @Valid ChatRoomRequest chatRoomRequest) {
        ChatRoom chatRoom = chatRoomService.addChatRoom(chatRoomRequest, loginUser.getId());
        ChatRoomResponse chatRoomResponse = new ChatRoomResponse(chatRoom.getUserId(), chatRoom.getId(),
                chatRoom.getTitle());
        return ResponseEntity.ok(chatRoomResponse);
    }

    @DeleteMapping("/{chatRoomId}")
    @Operation(summary = "채팅방 삭제", description = "채팅방 Id를 통해 채팅방 삭제",
            security = @SecurityRequirement(name = "JWT"))
    public ResponseEntity<Void> deleteChatRoom(@Parameter(hidden = true) @LoginUser Member loginUser,
                                               @PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId, loginUser.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "채팅방 조회", description = "날짜를 통해 채팅방 조회",
            security = @SecurityRequirement(name = "JWT"))
    public ResponseEntity<ChatRoomResponse> getChatRoom(@Parameter(hidden = true) @LoginUser Member loginUser,
                                                        @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        ChatRoomResponse response = chatRoomService.getChatRoomByDate(localDate, loginUser.getId());
        return ResponseEntity.ok(response);

    }

}
