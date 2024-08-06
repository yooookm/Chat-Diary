package com.example.diary_chat.resolver;

import com.example.diary_chat.domain.Member;
import com.example.diary_chat.exception.ForbiddenException;
import com.example.diary_chat.service.JwtService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtService jwtService;

    public LoginUserArgumentResolver(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(LoginUser.class) != null &&
                Member.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorizationHeader = webRequest.getHeader("Authorization");
        if (authorizationHeader != null) {
            Member appUser = jwtService.parse(authorizationHeader);
            if (appUser != null) {
                return appUser;
            }
            throw new ForbiddenException("활성화되지 않은 계정이거나 존재하지 않는 사용자입니다.");
        }
        throw new SecurityException();
    }
}
