package com.example.diary_chat.controller;

import com.example.diary_chat.domain.Diary;
import com.example.diary_chat.domain.Member;
import com.example.diary_chat.resolver.LoginUser;
import com.example.diary_chat.service.DiaryService;
import com.example.diary_chat.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.io.IOException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/api/diaries/photos")
public class S3Controller {

    private final S3Service s3Service;
    private final DiaryService diaryService;

    @Autowired
    public S3Controller(S3Service s3Service, DiaryService diaryService) {
        this.s3Service = s3Service;
        this.diaryService = diaryService;
    }

    @Operation(summary = "일기 이미지 업로드", description = "일기 이미지를 업로드합니다.",
            security = @SecurityRequirement(name = "JWT"))
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPetImage(
            @RequestParam Long diaryId,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile,
            @Parameter(hidden = true) @LoginUser Member loginUser
    ) throws IOException {
        if (multipartFile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일이 제공되지 않았습니다.");
        }

        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 확장자가 없습니다.");
        }

        String extend = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Set<String> allowedExtensions = Set.of(".jpg", ".png", ".jpeg");

        if (!allowedExtensions.contains(extend)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("허용되지 않는 파일 형식입니다.");
        }

        Diary diary = diaryService.findDiaryById(diaryId, loginUser.getId());

        String fileName = loginUser.getId() + "_" + diary.getDate();
        diary.setImageUrl(fileName + extend);
        diaryService.save(diary);
        String url = s3Service.upload(fileName, multipartFile, extend);
        log.info(url);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<byte[]> getPetImage(
//            @RequestParam String date,
//            @LoginUser Member loginUser
//    ) throws IOException {
//        LocalDate localDate = LocalDate.parse(date);
//
//        String fileName = loginUser.getId() + "_" + localDate + ".png";
//        log.info(fileName);
//        return s3Service.download(fileName);
//    }

    @Operation(summary = "일기 이미지 URL 조회", description = "일기 이미지 URL을 조회합니다.",
            security = @SecurityRequirement(name = "JWT"))
    @GetMapping
    public ResponseEntity<String> getPetImageUrl(
            @RequestParam Long diaryId,
            @Parameter(hidden = true) @LoginUser Member loginUser
    ) {
        Diary diary = diaryService.findDiaryById(diaryId, loginUser.getId());
        String fileName = diary.getImageUrl();
        if (fileName == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이미지가 없습니다.");
        }

        ResponseEntity<String> response = s3Service.downloadUrl(fileName);

        return response;
    }

}
