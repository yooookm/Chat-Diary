package com.example.diary_chat.repository;

import com.example.diary_chat.domain.ChatRoom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByUserId(Long userId);

    @Query("SELECT c FROM ChatRoom c WHERE c.userId = :userId AND c.date = :date AND c.status = true")
    Optional<List<ChatRoom>> findActiveChatRoomsByUserIdAndDate(@Param("userId") Long userId,
                                                                @Param("date") LocalDate date);
}
