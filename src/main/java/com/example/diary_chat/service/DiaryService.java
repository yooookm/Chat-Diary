package com.example.diary_chat.service;

import com.example.diary_chat.domain.ChatMessage;
import com.example.diary_chat.domain.ChatRoom;
import com.example.diary_chat.domain.Diary;
import com.example.diary_chat.domain.Emotion;
import com.example.diary_chat.dto.ChatGPTResponse;
import com.example.diary_chat.dto.DiaryRequest;
import com.example.diary_chat.dto.DiaryResponse;
import com.example.diary_chat.dto.EmotionStatisticsDto;
import com.example.diary_chat.dto.KeywordsResponse;
import com.example.diary_chat.dto.Message;
import com.example.diary_chat.dto.SevenEmotionDto;
import com.example.diary_chat.exception.DiaryNotFoundException;
import com.example.diary_chat.exception.ForbiddenException;
import com.example.diary_chat.repository.DiaryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final ChatGPTService chatGPTService;

    public DiaryService(DiaryRepository diaryRepository, ChatGPTService chatGPTService) {
        this.diaryRepository = diaryRepository;
        this.chatGPTService = chatGPTService;
    }

    public List<DiaryResponse> getAllDiariesByUserId(Long userId) {
        return diaryRepository.findAllByUserId(userId).stream()
                .map(diary -> new DiaryResponse(diary))
                .collect(Collectors.toList());
    }

    public Diary findDiaryById(Long id, Long userId) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Diary Id:" + id));

        if (!diary.isDiaryOwner(userId)) {
            throw new ForbiddenException("해당 일기에 대한 권한이 없습니다.");
        }

        return diary;

    }

    public DiaryResponse getDiaryById(Long id, Long userId) {
        Diary diary = findDiaryById(id, userId);

        return new DiaryResponse(diary);

    }

    public DiaryResponse getDiaryByDate(LocalDate date, Long userId) {
        List<Diary> diaries = diaryRepository.findActiveDiariesByUserIdAndDate(userId, date)
                .orElseThrow(() -> new DiaryNotFoundException("해당하는 날짜에 일기가 없습니다"));

        if (diaries.isEmpty()) {
            throw new DiaryNotFoundException("해당하는 날짜에 일기가 없습니다");
        }

        return new DiaryResponse(diaries.get(diaries.size() - 1));
    }

//    public Diary addDiary(DiaryRequest diaryRequest, Long userId) {
//        // userId 검증 로직 필요
//        Diary diary = new Diary(userId, diaryRequest.title(), diaryRequest.content(), diaryRequest.date(),
//                diaryRequest.emotion());
//        return diaryRepository.save(diary);
//    }

    public void updateDiary(DiaryRequest diaryRequest, Long diaryId, Long userId) {

        Diary diary = findDiaryById(diaryId, userId);

        diary.updateDiary(diaryRequest.title(), diaryRequest.content(), diaryRequest.date(), diaryRequest.emotion());
        diaryRepository.save(diary);
    }

    public void deleteDiary(Long diaryId, Long userId) {
        Diary diary = findDiaryById(diaryId, userId);
        diaryRepository.delete(diary);
    }

    public List<EmotionStatisticsDto> getMonthlyEmotionStatistics(Long userId, String yearMonth) {
        YearMonth month = YearMonth.parse(yearMonth);
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        List<Diary> diaries = diaryRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        // 모든 감정에 대해 기본 값 0으로 초기화된 맵 생성
        Map<Emotion, Long> emotionCountMap = Arrays.stream(Emotion.values())
                .collect(Collectors.toMap(e -> e, e -> 0L));

        // 실제 일기 데이터를 기반으로 감정 카운트 업데이트
        diaries.stream()
                .collect(Collectors.groupingBy(Diary::getEmotion, Collectors.counting()))
                .forEach((emotion, count) -> emotionCountMap.put(emotion, count));

        // 결과 리스트 생성
        return emotionCountMap.entrySet().stream()
                .map(entry -> new EmotionStatisticsDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    //7일 전, 현재, 7일 후 감정 조회 메서드
    public List<SevenEmotionDto> getDiariesForDateRange(Long userId, LocalDate targetDate) {
        LocalDate startDate = targetDate.minusDays(7);
        LocalDate endDate = targetDate.plusDays(7).isAfter(LocalDate.now()) ? LocalDate.now() : targetDate.plusDays(7);

        List<Diary> diaries = diaryRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        return diaries.stream()
                .map(diary -> new SevenEmotionDto(diary.getUserId(), diary.getDate(), diary.getEmotion()))
                .collect(Collectors.toList());
    }

    // 일기 생성 관련 로직
    public List<Message> createChatHistory(ChatRoom chatRoom) {
        return chatRoom.getChatHistory().stream()
                .map(chatMessage -> new Message(chatMessage.getRole() == ChatMessage.Role.USER ? "user" : "assistant",
                        chatMessage.getContent()))
                .collect(Collectors.toList());
    }

    public String generateDiaryContent(List<Message> chatHistory) {
        chatHistory.add(new Message("system", "위 대화를 7줄 이하 일기 형식으로 요약해 주세요. 사용자 입장에서 일기를 작성해줘"));
        ChatGPTResponse contentResponse = chatGPTService.getChatGPTResponse(chatHistory);
        return contentResponse.getChoices().get(0).getMessage().getContent();
    }

    public String generateDiaryTitle(String diaryContent) {
        List<Message> titleMessages = List.of(
                new Message("system", "suggest an appropriate title from the following diary."),
                new Message("system", "Answer in korean."),
                new Message("user", diaryContent)
        );
        ChatGPTResponse titleResponse = chatGPTService.getChatGPTResponse(titleMessages);
        return titleResponse.getChoices().get(0).getMessage().getContent().trim();
    }

    public Emotion extractPrimaryEmotion(String diaryContent) {
        List<Message> emotionMessages = List.of(
                new Message("system",
                        "Extract one primary emotion keyword from the following diary. The keyword should match one of the following: 화남, 불안, 행복, 즐거움, 당황, 힘듦, 슬픔, 보통."),
                new Message("system", "Your responses should be limited to one emotion word."),
                new Message("system", "Answer in korean."),
                new Message("user", diaryContent)
        );
        ChatGPTResponse emotionResponse = chatGPTService.getChatGPTResponse(emotionMessages);
        String emotionKeyword = emotionResponse.getChoices().get(0).getMessage().getContent().trim();
        return Emotion.fromKorean(emotionKeyword);
    }

    public KeywordsResponse extractKeywords(String diaryContent) {
        List<Message> keywordMessages = List.of(
                new Message("system",
                        "Extract three primary keywords from the following diary. The keywords should be related to the main emotions or themes present in the diary."),
                new Message("system", "Your responses should be limited to three keywords, separated by commas."),
                new Message("system", "Answer in korean."),
                new Message("user", diaryContent)
        );
        ChatGPTResponse keywordResponse = chatGPTService.getChatGPTResponse(keywordMessages);
        String keywords = keywordResponse.getChoices().get(0).getMessage().getContent().trim();
        String[] keywordArray = keywords.split(",");
        return new KeywordsResponse(
                keywordArray.length > 0 ? keywordArray[0].trim() : "",
                keywordArray.length > 1 ? keywordArray[1].trim() : "",
                keywordArray.length > 2 ? keywordArray[2].trim() : ""
        );
    }

    public void save(Diary diary) {
        diaryRepository.save(diary);
    }
}