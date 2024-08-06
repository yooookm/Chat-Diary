package com.example.diary_chat.domain;

import com.example.diary_chat.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@Table(name = "chat_room")
@SQLDelete(sql = "UPDATE chat_room SET status = false WHERE id = ?")
// @SQLRestriction("status = true")
@Where(clause = "status = true")
@NoArgsConstructor
public class ChatRoom extends BaseEntity {
    private Long userId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatHistory = new ArrayList<>();

    public ChatRoom(Long userId, String title, LocalDate date) {
        this.userId = userId;
        this.title = title;
        this.date = date;
    }

    public boolean isOwnedBy(Long userId) {
        return this.userId.equals(userId);
    }
}
