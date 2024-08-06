package com.example.diary_chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record DiaryRequest(
        @NotBlank
        @Size(max = 100)
        String title,

        @NotBlank
        @Size(max = 1000)
        String content,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate date,

        @NotBlank
        String emotion
) {
}
