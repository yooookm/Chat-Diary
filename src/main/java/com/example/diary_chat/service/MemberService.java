package com.example.diary_chat.service;


import com.example.diary_chat.domain.Member;
import com.example.diary_chat.dto.JoinMemberRequest;
import com.example.diary_chat.dto.LoginMemberRequest;
import com.example.diary_chat.exception.member.MemberCustomException.DuplicatedEmailException;
import com.example.diary_chat.exception.member.MemberCustomException.DuplicatedNicknameException;
import com.example.diary_chat.exception.member.MemberCustomException.EmailNotFoundException;
import com.example.diary_chat.exception.member.MemberCustomException.IncorrectPasswordException;
import com.example.diary_chat.exception.member.MemberCustomException.InvalidEmailFormatException;
import com.example.diary_chat.exception.member.MemberCustomException.InvalidNicknameLengthException;
import com.example.diary_chat.exception.member.MemberCustomException.MissingInformationException;
import com.example.diary_chat.exception.member.MemberCustomException.PasswordTooShortException;
import com.example.diary_chat.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private final MemberRepository memberRepository;

    public Member loginMember(LoginMemberRequest request) {
        String loginEmail = request.email();
        String loginPassword = request.password();
        Member member = memberRepository.findByEmail(loginEmail)
                .orElseThrow(EmailNotFoundException::new);

        checkMissingInformationLogin(loginEmail, loginPassword);

        if (!Objects.equals(loginPassword, member.getPassword())) {
            throw new IncorrectPasswordException();
        }
        log.info("로그인 성공");
        return member;
    }

    @Transactional
    public void joinMember(JoinMemberRequest request) {
        String joinnickname = request.nickname();
        String joinemail = request.email();
        String joinpassword = request.password();

        checkMissingInformationJoin(joinnickname, joinemail, joinpassword);
        checkDuplicateNickname(joinnickname);
        checkNicknameLength(joinnickname);
        checkDuplicateEmail(joinemail);
        InvalidateEmailFormat(joinemail);
        checkPasswordLength(joinpassword);

        Member member = new Member(joinnickname, joinemail, joinpassword);
        memberRepository.save(member);
    }

    //함수들

    private void checkMissingInformationJoin(String nickname, String email, String password) {
        if (nickname == null || nickname.isBlank() || email == null || email.isBlank() || password == null
                || password.isBlank()) {
            throw new MissingInformationException();
        }
    }

    private void checkMissingInformationLogin(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new MissingInformationException();
        }
    }

    private void checkDuplicateNickname(String nickname) {
        Optional<Member> optionalNickname = memberRepository.findByNickname(nickname);
        if (optionalNickname.isPresent()) {
            throw new DuplicatedNicknameException();
        }
    }

    private void checkNicknameLength(String nickname) {
        if (nickname.length() < 2 || nickname.length() > 10) {
            throw new InvalidNicknameLengthException();
        }
    }

    private void InvalidateEmailFormat(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailFormatException();
        }
    }

    private void checkDuplicateEmail(String email) {
        Optional<Member> optionalEmail = memberRepository.findByEmail(email);
        if (optionalEmail.isPresent()) {
            throw new DuplicatedEmailException();
        }
    }

    private void checkPasswordLength(String password) {
        if (password.length() < 6) {
            throw new PasswordTooShortException();
        }
    }
}