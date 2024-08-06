package com.example.diary_chat.controller;


import com.example.diary_chat.domain.Member;
import com.example.diary_chat.dto.JoinMemberRequest;
import com.example.diary_chat.dto.LoginMemberRequest;
import com.example.diary_chat.dto.MemberInfoResponse;
import com.example.diary_chat.resolver.LoginUser;
import com.example.diary_chat.service.JwtService;
import com.example.diary_chat.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Tag(name = "Members", description = "Members API")
public class MemberController {

    private final JwtService jwtService;
    private final MemberService memberService;


    @PostMapping("/login")
    @Operation(summary = "회원 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "정보를 모두 입력해주세요", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "비밀번호 불일치", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이메일", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> login(@RequestBody LoginMemberRequest request) {
        Member member = memberService.loginMember(request);
        String token = jwtService.create(member.getEmail());
        return ResponseEntity.ok().header("Authorization", token).body("로그인 되었습니다");
    }


    @PostMapping("/join")
    @Operation(summary = "회원 가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "가입 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "정보를 모두 입력해주세요 / 닉네임은 2~10글자 사이여야 합니다 / 유효한 이메일 형식이어야 합니다 / 비밀번호를 6자리 이상으로 해주세요", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "닉네임 중복 / 이메일 중복", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> join(@RequestBody JoinMemberRequest request) {
        memberService.joinMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("가입 완료");
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "로그인한 회원의 정보 조회",
            security = @SecurityRequirement(name = "JWT"))
    public ResponseEntity<MemberInfoResponse> getMyInfo(@Parameter(hidden = true) @LoginUser Member loginUser) {
        return ResponseEntity.ok().body(new MemberInfoResponse(loginUser.getId(), loginUser.getNickname(),
                loginUser.getEmail()));
    }
}
