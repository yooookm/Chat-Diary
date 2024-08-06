package com.example.diary_chat.domain;

import com.example.diary_chat.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@SQLDelete(sql = "UPDATE diary SET status = false WHERE id = ?")
// @SQLRestriction("status = true")
@Where(clause = "status = true")
@NoArgsConstructor
@Table(name = "diary")
public class Diary extends BaseEntity {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    @Column
    private String keywords1;
    @Column
    private String keywords2;
    @Column
    private String keywords3;

    private String imageUrl;

//    public Diary(Long userId, String title, String content, LocalDate date, String emotion) {
//        this.userId = userId;
//        this.title = title;
//        this.content = content;
//        this.date = date;
//        this.emotion = Emotion.fromKorean(emotion);
//    }

    public Diary(Long userId, String title, String content, LocalDate date, Emotion emotion, String keywords1,
                 String keywords2, String keywords3) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = date;
        this.emotion = emotion;
        this.keywords1 = keywords1;
        this.keywords2 = keywords2;
        this.keywords3 = keywords3;
    }

    public void updateDiary(String title, String content, LocalDate date, String emotion) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.emotion = Emotion.fromKorean(emotion);
    }

    public boolean isDiaryOwner(Long userId) {
        return this.userId.equals(userId);
    }

}