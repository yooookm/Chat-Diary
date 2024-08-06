package com.example.diary_chat.service;

import com.example.diary_chat.domain.ChatRoom;
import com.example.diary_chat.dto.ChatRoomRequest;
import com.example.diary_chat.dto.ChatRoomResponse;
import com.example.diary_chat.exception.DiaryNotFoundException;
import com.example.diary_chat.exception.ForbiddenException;
import com.example.diary_chat.repository.ChatRoomRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom getChatRoomById(Long id, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ChatRoom Id:" + id));

        if (!chatRoom.isOwnedBy(userId)) {
            throw new ForbiddenException("해당 채팅방에 대한 권한이 없습니다.");
        }

        return chatRoom;
    }

    public ChatRoomResponse getChatRoomByDate(LocalDate date, Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findActiveChatRoomsByUserIdAndDate(userId, date)
                .orElseThrow(() -> new DiaryNotFoundException("해당하는 날짜에 채팅방이 없습니다"));

        if (chatRooms.isEmpty()) {
            throw new DiaryNotFoundException("해당하는 날짜에 일기가 없습니다");
        }

        ChatRoom chatRoom = chatRooms.get(chatRooms.size() - 1);

        return new ChatRoomResponse(chatRoom.getUserId(), chatRoom.getId(), chatRoom.getTitle());

    }


    public List<ChatRoom> getAllChatRoomsByUserId(Long userId) {
        return chatRoomRepository.findAllByUserId(userId);
    }

    public ChatRoom addChatRoom(ChatRoomRequest chatRoomRequest, Long userId) {
        ChatRoom chatRoom = new ChatRoom(userId, chatRoomRequest.title(), chatRoomRequest.date());
        return chatRoomRepository.save(chatRoom);
    }

    public void deleteChatRoom(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = getChatRoomById(chatRoomId, userId);
        chatRoomRepository.delete(chatRoom);
    }

    public void saveChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
    }
}
