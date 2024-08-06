package com.example.diary_chat.controller;

import com.example.diary_chat.repository.QueryRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/queries")
public class QueryController {
    private final QueryRepository queryRepository;

    public QueryController(QueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    @GetMapping("/random")
    @Operation(summary = "일기 시작 질문 1개 랜덤 조회")
    public ResponseEntity<String> getRandomQuery() {
        return ResponseEntity.ok(queryRepository.findRandomQuery().getQuery());
    }
}
