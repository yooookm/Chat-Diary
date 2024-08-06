package com.example.diary_chat.service;


import com.example.diary_chat.domain.Member;
import com.example.diary_chat.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final MemberRepository memberRepository;

    @Value("${jwt.test}")
    private String secretKey;

    public String create(String email) {

        //암호화하기
        String jwt = "Bearer " + Jwts.builder()
                .subject("potato jwt service")
                .issuer("potato server")
                .audience()
                .and().id(email)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .claim("email", email)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();

        return jwt;
    }

    public Member parse(String token) {
        try {
            String email = getEmail(token);
            return memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        } catch (ExpiredJwtException e) {
            log.error("토큰 만료", e);
            throw new IllegalArgumentException("토큰 만료", e);
        } catch (UnsupportedJwtException e) {
            log.error("미지원 토큰", e);
            throw new IllegalArgumentException("미지원 토큰", e);
        } catch (MalformedJwtException e) {
            log.error("토큰 형식 오류", e);
            throw new IllegalArgumentException("토큰 형식 오류", e);
        } catch (SignatureException e) {
            log.error("유효하지 않은 토큰 서명", e);
            throw new IllegalArgumentException("유효하지 않은 토큰 서명", e);
        }
    }


    //복호화하기
    private String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build().parseSignedClaims(token.substring(7)).getPayload().get("email", String.class);
    }
}
