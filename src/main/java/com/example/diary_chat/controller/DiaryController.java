package com.example.diary_chat.controller;

import com.example.diary_chat.domain.Member;
import com.example.diary_chat.dto.DiaryRequest;
import com.example.diary_chat.dto.DiaryResponse;
import com.example.diary_chat.dto.EmotionStatisticsDto;
import com.example.diary_chat.dto.SevenEmotionDto;
import com.example.diary_chat.resolver.LoginUser;
import com.example.diary_chat.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diaries")
@Tag(name = "Diary", description = "Diary API")
public class DiaryController {

    private final DiaryService diaryService;

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @GetMapping
    @Operation(summary = "일기 전체 조회",
            security = @SecurityRequirement(name = "JWT")
    )
    public ResponseEntity<List<DiaryResponse>> getAllDiariesByUserId(
            @Parameter(hidden = true) @LoginUser Member loginUser) {
        List<DiaryResponse> diaries = diaryService.getAllDiariesByUserId(loginUser.getId());
        return ResponseEntity.ok(diaries);
    }

    @GetMapping("/{id}")
    @Operation(summary = "일기 세부 조회(id)", description = "일기 Id를 통해 일부 세부 내용 조회",
            security = @SecurityRequirement(name = "JWT"))
    public ResponseEntity<DiaryResponse> getDiaryById(@Parameter(hidden = true) @LoginUser Member loginUser,
                                                      @PathVariable Long id) {
        DiaryResponse diary = diaryService.getDiaryById(id, loginUser.getId());
        return ResponseEntity.ok(diary);
    }

    @GetMapping("/date")
    @Operation(summary = "일기 세부 조회 (날짜)", description = "날짜를 통해 일부 세부 내용 조회",
            security = @SecurityRequirement(name = "JWT"))
    public ResponseEntity<DiaryResponse> getDiaryByDate(@Parameter(hidden = true) @LoginUser Member loginUser,
                                                        @RequestParam String targetDate) {
        LocalDate date = LocalDate.parse(targetDate);
        DiaryResponse diary = diaryService.getDiaryByDate(date, loginUser.getId());
        return ResponseEntity.ok(diary);
    }

//    @PostMapping
//    @Operation(summary = "일기 생성(작성)")
//    public ResponseEntity<Diary> addDiary(@RequestBody @Valid DiaryRequest diaryRequest) {
//        Long userId = 1L; // userId 추출 로직 필요
//        Diary diary = diaryService.addDiary(diaryRequest, userId);
//        return ResponseEntity.ok(diary);
//    }

    @PutMapping("/{id}")
    @Operation(summary = "일기 수정", description = "일기 Id를 통해 일부 세부 내용 수정",
            security = @SecurityRequirement(name = "JWT"))
    public ResponseEntity<Void> updateDiary(@Parameter(hidden = true) @LoginUser Member loginUser,
                                            @PathVariable Long id,
                                            @RequestBody @Valid DiaryRequest diaryRequest) {
        diaryService.updateDiary(diaryRequest, id, loginUser.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{diaryId}/user")
    @Operation(summary = "일기 삭제", description = "일기 Id를 통해 일기 삭제",
            security = @SecurityRequirement(name = "JWT"))
    public ResponseEntity<Void> deleteDiary(@Parameter(hidden = true) @LoginUser Member loginUser,
                                            @PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId, loginUser.getId());
        return ResponseEntity.ok().build();
    }

    //월별 감정 통계 조회하기
    @GetMapping("/emotion/months")
    @Operation(summary = "월별 감정 전체 조회",
            security = @SecurityRequirement(name = "JWT"))
    public ResponseEntity<List<EmotionStatisticsDto>> getMonthlyEmotionStatistics(
            @Parameter(hidden = true) @LoginUser Member loginUser,
            @RequestParam String yearMonth) {
        List<EmotionStatisticsDto> statistics = diaryService.getMonthlyEmotionStatistics(loginUser.getId(), yearMonth);
        return ResponseEntity.ok(statistics);
    }

    //7일 전, 현재, 7일 후 각각의 감정 조회하기
    @GetMapping("/emotion/range")
    @Operation(summary = "감정 범위 조회(+-7일)", description = "7일 전, 현재, 7일 후 총 15개 조회",
            security = @SecurityRequirement(name = "JWT"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식입니다", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> getDiariesForDateRange(@Parameter(hidden = true) @LoginUser Member loginUser,
                                                    @RequestParam String targetDate) {
        try {
            LocalDate date = LocalDate.parse(targetDate);
            List<SevenEmotionDto> diaries = diaryService.getDiariesForDateRange(loginUser.getId(), date);
            return ResponseEntity.ok(diaries);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 날짜 형식입니다.");
        }
    }
}