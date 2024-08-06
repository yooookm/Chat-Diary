package com.example.diary_chat.dto;

import com.example.diary_chat.domain.Diary;
import com.example.diary_chat.domain.Emotion;
import java.time.LocalDate;
import lombok.Data;

@Data
public class DiaryResponse {
    private Long id;
    private String title;
    private String content;
    private Emotion emotion;
    private LocalDate date;
    private String keyword1;
    private String keyword2;
    private String keyword3;

    public DiaryResponse(Diary diary) {
        this.id = diary.getId();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.emotion = diary.getEmotion();
        this.date = diary.getDate();
        this.keyword1 = diary.getKeywords1();
        this.keyword2 = diary.getKeywords2();
        this.keyword3 = diary.getKeywords3();
    }
}

