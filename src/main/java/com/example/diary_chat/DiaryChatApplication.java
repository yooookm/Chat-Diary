package com.example.diary_chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DiaryChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiaryChatApplication.class, args);
    }

}
