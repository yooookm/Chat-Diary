package com.example.diary_chat.domain;

import com.example.diary_chat.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@SQLDelete(sql = "UPDATE chat_message SET status = false WHERE id = ?")
@Where(clause = "status = true")
@NoArgsConstructor
public class ChatMessage extends BaseEntity {
    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public ChatMessage(String content, ChatRoom chatRoom,Role role) {
        this.content = content;
        this.chatRoom = chatRoom;
        this.role = role;
    }
    public enum Role {
        USER, ASSISTANT
    }
}