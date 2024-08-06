package com.example.diary_chat.repository;

import com.example.diary_chat.domain.Diary;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findAllByUserId(Long userId);

    List<Diary> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT d FROM Diary d WHERE d.userId = :userId AND d.date = :date AND d.status = true")
    Optional<List<Diary>> findActiveDiariesByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

}