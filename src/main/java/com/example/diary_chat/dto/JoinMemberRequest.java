package com.example.diary_chat.dto;

import jakarta.validation.constraints.NotBlank;


public record JoinMemberRequest(

        @NotBlank
        String nickname,

        @NotBlank
        String email,

        @NotBlank
        String password) {
}
