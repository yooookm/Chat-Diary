package com.example.diary_chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ChatRoomRequest(
        @NotBlank
        @Size(max = 100)
        String title,

        @NotNull
        LocalDate date
) {
}
